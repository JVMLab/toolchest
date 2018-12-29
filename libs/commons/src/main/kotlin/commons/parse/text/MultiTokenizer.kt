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
    defaultTokenType: E,
    private val subTokenizers: List<AbstractTokenizer<E>>
) : AbstractTokenizer<E>(defaultTokenType) {

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
  private fun updateTokenizer(): BuildingDetails {
    processEmptyOrSingleActiveTokenizer()?.let { return it}

    // Iterate over activeTokenizers and keep the only BUILDING and FINISHED statuses
    val iterator = activeTokenizers.iterator()
    while (iterator.hasNext()) {
      val details = iterator.next().getRTokenBuilder().details
      when (details.status) {
        BuildingStatus.NONE ->throw IllegalStateException(
            "Unexpected ${BuildingStatus.NONE} of an active sub-tokenizer")
        BuildingStatus.FAILED -> return details
        BuildingStatus.CANCELLED -> iterator.remove()
        BuildingStatus.BUILDING, BuildingStatus.FINISHED -> {}
      }
    }

    // At this stage activeTokenizers contains the only BUILDING and FINISHED statuses
    processEmptyOrSingleActiveTokenizer()?.let { return it}

    // If at this stage activeTokenizers contains a FINISHED status then it's an ambiguous parsing
    // error because it contains more than one element and other elements could have only
    // BUILDING or FINISHED statuses
    (activeTokenizers.find { it.getBuildingStatus() == BuildingStatus.FINISHED })?.let {
      setTokenType(defaultTokenType)
      nextCharIncluded = (it.getRTokenBuilder().finish > getRTokenBuilder().finish)
      return BuildingDetails(BuildingStatus.FAILED,
          "${it.getRTokenBuilder().type} sub-tokenizer is ${BuildingStatus.FINISHED} but another " +
              "has either ${BuildingStatus.BUILDING} or ${BuildingStatus.FINISHED} status")
    }

    // At this stage activeTokenizers contains more than one element,
    // and they have the only BUILDING statuses
    // Set defaultTokenType of the tokenBuilder because we have more then one active tokenizer
    setTokenType(defaultTokenType)
    nextCharIncluded = true // BuildingStatus.BUILDING always includes a next char
    return BuildingDetails(BuildingStatus.BUILDING)
  }


  /**
   * Used in [updateTokenizer] to process [activeTokenizers] with 0 or 1 element
   */
  private fun processEmptyOrSingleActiveTokenizer(): BuildingDetails? {
    if (activeTokenizers.isEmpty()) return BuildingDetails()

    if (activeTokenizers.size == 1) {
      val activeTokenizer = activeTokenizers.first
      check(activeTokenizer.getBuildingStatus() != BuildingStatus.NONE) {
        "Unexpected ${BuildingStatus.NONE} of an active sub-tokenizer"
      }
      // Successful BuildingStatus.FINISHED is returned from here
      setTokenType(activeTokenizer.getRTokenBuilder().type)
      nextCharIncluded = (activeTokenizer.getRTokenBuilder().finish > getRTokenBuilder().finish)
      return activeTokenizer.getRTokenBuilder().details
    }
    return null
  }


  override fun firstChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    // populate activeTokenizers
    subTokenizers.forEach { subTokenizer: AbstractTokenizer<E> ->
      subTokenizer.processChar(char, idx, isLast)
      when (subTokenizer.getBuildingStatus()) {
        BuildingStatus.FAILED -> {
          setTokenType(subTokenizer.getRTokenBuilder().type)
          return subTokenizer.getRTokenBuilder().details
        }
        BuildingStatus.BUILDING, BuildingStatus.FINISHED, BuildingStatus.CANCELLED ->
          activeTokenizers.add(subTokenizer)
        BuildingStatus.NONE -> {}
      }
    }

    // process activeTokenizers and return result
    return updateTokenizer()
  }


  override fun nextChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    // we should have some active tokenizers if this method is called
    check(activeTokenizers.isNotEmpty()) { "Unexpected empty list of active sub-tokenizers" }

    // process char in all active tokenizers
    activeTokenizers.forEach {
      // we should have the only BUILDING statuses in activeTokenizers
      check(it.getBuildingStatus() == BuildingStatus.BUILDING) {
        "Unexpected ${it.getBuildingStatus()} of an active ${it.getRTokenBuilder().type} " +
            "sub-tokenizer"
      }
      it.processChar(char, idx, isLast)
    }

    // process activeTokenizers and return result
    return updateTokenizer()
  }
}
