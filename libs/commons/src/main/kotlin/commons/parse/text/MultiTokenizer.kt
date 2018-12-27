package com.jvmlab.commons.parse.text

import java.lang.IllegalStateException
import java.util.*


/**
 * Represents an [AbstractTokenizer] which produces tokens of multiple types by delegating
 * the actual work to sub-tokenizers
 *
 * @property defaultTokenType is a [Token] type used to create a [TokenBuilder] when a type cannot
 * be determined from a sub-tokenizer, for example in case of an ambiguous tokenization result
 * @property subTokenizers is a [List] of sub-tokenizers to produce actual tokens
 * @property activeTokenizers is a [LinkedList] of sub-tokenizers with [BuildingStatus]
 * different from [BuildingStatus.NONE] or [BuildingStatus.CANCELLED]
 */
open class MultiTokenizer<E: Enum<E>>(
    private val defaultTokenType: E,
    private val subTokenizers: List<AbstractTokenizer<E>>
) : AbstractTokenizer<E>() {

  private val activeTokenizers: LinkedList<AbstractTokenizer<E>> = LinkedList()


  /**
   * Resets all [subTokenizers] and clears [activeTokenizers]
   */
  override fun reset() {
    super.reset()
    activeTokenizers.clear()
    subTokenizers.forEach { it.reset() }
  }


  /**
   * Checks [BuildingStatus] of [activeTokenizers], removes tokenizers with
   * [BuildingStatus.CANCELLED] (if there is no tokenizers with [BuildingStatus.FAILED] or any
   * other errors) and updates [tokenBuilder] accordingly
   *
   * [activeTokenizers] should have the only tokenizers with [BuildingStatus.BUILDING] or be empty
   * as a result of using this function
   *
   * Used in [firstChar] and [nextChar] methods after a change in [activeTokenizers]
   */
  private fun updateTokenizer() {
    if (processEmptyOrSingleActiveTokenizer()) return

    // Iterate over activeTokenizers and keep the only BUILDING and FINISHED statuses
    val iterator = activeTokenizers.iterator()
    while (iterator.hasNext()) {
      val tokenizer = iterator.next()
      when (tokenizer.getBuildingStatus()) {
        BuildingStatus.NONE ->throw IllegalStateException(
            "Unexpected ${BuildingStatus.NONE} of an active sub-tokenizer")
        BuildingStatus.FAILED -> {
          tokenBuilder = tokenizer.tokenBuilder
          activeTokenizers.clear()
          return
        }
        BuildingStatus.CANCELLED -> iterator.remove()
        BuildingStatus.BUILDING, BuildingStatus.FINISHED -> {}
      }
    }

    // At this stage activeTokenizers contains the only BUILDING and FINISHED statuses
    if (processEmptyOrSingleActiveTokenizer()) return

    // If at this stage activeTokenizers contains a FINISHED status then it's an ambiguous parsing
    // error because it contains more than one element and other elements could have only
    // BUILDING or FINISHED statuses
    (activeTokenizers.find { it.getBuildingStatus() == BuildingStatus.FINISHED })?.let {
      reset()
      tokenBuilder = it.tokenBuilder?.transformType(defaultTokenType)
      tokenBuilder?.status = BuildingStatus.FAILED
      return
    }

    // At this stage activeTokenizers contains the only BUILDING statuses
    // Set defaultTokenType of the tokenBuilder because we have more then one active tokenizer
    tokenBuilder = activeTokenizers.first.tokenBuilder?.transformType(defaultTokenType)
  }


  /**
   * Used in [updateTokenizer] to process [activeTokenizers] with 0 or 1 element
   */
  private fun processEmptyOrSingleActiveTokenizer(): Boolean {
    if (activeTokenizers.isEmpty()) {
      super.reset()
      return true
    }

    if (activeTokenizers.size == 1) {
      tokenBuilder = activeTokenizers.first.tokenBuilder
      check(getBuildingStatus() != BuildingStatus.NONE) {
        "Unexpected ${BuildingStatus.NONE} of an active sub-tokenizer"
      }
      if (getBuildingStatus() != BuildingStatus.BUILDING)
        activeTokenizers.clear()
      return true
    }

    return false
  }


  override fun firstChar(char: Char, idx: Int, isLast: Boolean) {
    // populate activeTokenizers
    subTokenizers.forEach { tokenizer: AbstractTokenizer<E> ->
      tokenizer.processChar(char, idx, isLast)
      when (tokenizer.getBuildingStatus()) {
        BuildingStatus.FAILED -> {
          tokenBuilder = tokenizer.tokenBuilder
          return
        }
        BuildingStatus.BUILDING, BuildingStatus.FINISHED, BuildingStatus.CANCELLED ->
          activeTokenizers.add(tokenizer)
        BuildingStatus.NONE -> {}
      }
    }

    // process activeTokenizers and set tokenBuilder accordingly
    updateTokenizer()
  }


  override fun nextChar(char: Char, idx: Int, isLast: Boolean) {
    // we should have some active tokenizers if this method is called
    check(activeTokenizers.isNotEmpty()) { "Unexpected empty list of active sub-tokenizers" }

    // process char in all active tokenizers
    activeTokenizers.forEach {
      // we should have the only BUILDING statuses in activeTokenizers
      check(it.getBuildingStatus() == BuildingStatus.BUILDING) {
        "Unexpected ${it.getBuildingStatus()} of an active sub-tokenizer"
      }
      it.processChar(char, idx, isLast)
    }

    // process activeTokenizers and set tokenBuilder accordingly
    updateTokenizer()
  }
}
