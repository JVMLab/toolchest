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
 * different from [StatusNone] or [StatusCancelled]
 */
open class MultiTokenizer<E: Enum<E>>(
    defaultTokenType: E,
    private val subTokenizers: List<ITokenizer<E>>
) : AbstractTokenizer<E>(defaultTokenType) {

  private val activeTokenizers: LinkedList<ITokenizer<E>> = LinkedList()


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
   * [StatusCancelled] (if there is no tokenizers with [StatusFailed] or any
   * other errors) and updates [tokenBuilder] accordingly
   *
   * [activeTokenizers] should have the only tokenizers with [StatusBuilding] or be empty
   * as a result of using this function
   *
   * Used in [firstChar] and [nextChar] methods after a change in [activeTokenizers]
   */
  private fun updateTokenizer(): BuildingStatus {
    processEmptyOrSingleActiveTokenizer()?.let { return it}

    // Iterate over activeTokenizers and keep the only BUILDING and FINISHED statuses
    val iterator = activeTokenizers.iterator()
    while (iterator.hasNext()) {
      val status = iterator.next().getBuildingStatus()
      when (status) {
        StatusNone ->throw IllegalStateException(
            "Unexpected $StatusNone of an active sub-tokenizer")
        is StatusFailed -> return status
        StatusCancelled -> iterator.remove()
        StatusBuilding, StatusFinished -> {}
      }
    }

    // At this stage activeTokenizers contains the only BUILDING and FINISHED statuses
    processEmptyOrSingleActiveTokenizer()?.let { return it}

    // If at this stage activeTokenizers contains a FINISHED status then it's an ambiguous parsing
    // error because it contains more than one element and other elements could have only
    // BUILDING or FINISHED statuses
    (activeTokenizers.find { it.getBuildingStatus() == StatusFinished })?.let {
      setTokenType(defaultTokenType)
      finalCharIncluded = (it.getRTokenBuilder().finish > getRTokenBuilder().finish)
      return StatusFailed("${it.getRTokenBuilder().type} sub-tokenizer is $StatusFinished but another " +
              "has either $StatusBuilding or $StatusFinished status")
    }

    // At this stage activeTokenizers contains more than one element,
    // and they have the only BUILDING statuses
    // Set defaultTokenType of the tokenBuilder because we have more then one active tokenizer
    setTokenType(defaultTokenType)
    return StatusBuilding
  }


  /**
   * Used in [updateTokenizer] to process [activeTokenizers] with 0 or 1 element
   */
  private fun processEmptyOrSingleActiveTokenizer(): BuildingStatus? {
    if (activeTokenizers.isEmpty()) return StatusNone

    if (activeTokenizers.size == 1) {
      val activeTokenizer = activeTokenizers.first
      check(activeTokenizer.getBuildingStatus() != StatusNone) {
        "Unexpected $StatusNone of an active sub-tokenizer"
      }
      // Successful BuildingStatus.FINISHED is returned from here
      setTokenType(activeTokenizer.getRTokenBuilder().type)
      finalCharIncluded = (activeTokenizer.getRTokenBuilder().finish > getRTokenBuilder().finish)
      return activeTokenizer.getBuildingStatus()
    }
    return null
  }


  override fun firstChar(char: Char, idx: Int, isLast: Boolean): BuildingStatus {
    // populate activeTokenizers
    subTokenizers.forEach { subTokenizer: ITokenizer<E> ->
      subTokenizer.processChar(char, idx, isLast)
      when (subTokenizer.getBuildingStatus()) {
        is StatusFailed -> {
          setTokenType(subTokenizer.getRTokenBuilder().type)
          return subTokenizer.getBuildingStatus()
        }
        StatusBuilding, StatusFinished, StatusCancelled ->
          activeTokenizers.add(subTokenizer)
        StatusNone -> {}
      }
    }

    // process activeTokenizers and return result
    return updateTokenizer()
  }


  override fun nextChar(char: Char, idx: Int, isLast: Boolean): BuildingStatus {
    // we should have some active tokenizers if this method is called
    check(activeTokenizers.isNotEmpty()) { "Unexpected empty list of active sub-tokenizers" }

    // process char in all active tokenizers
    activeTokenizers.forEach {
      // we should have the only BUILDING statuses in activeTokenizers
      check(it.getBuildingStatus() == StatusBuilding) {
        "Unexpected ${it.getBuildingStatus()} of an active ${it.getRTokenBuilder().type} " +
            "sub-tokenizer"
      }
      it.processChar(char, idx, isLast)
    }

    // process activeTokenizers and return result
    return updateTokenizer()
  }
}
