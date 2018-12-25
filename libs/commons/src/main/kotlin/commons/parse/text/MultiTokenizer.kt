package com.jvmlab.commons.parse.text

import java.util.*


/**
 * Represents an [AbstractTokenizer] which produces tokens of multiple types by delegating
 * the actual work to sub-tokenizers
 *
 * @property subTokenizers is a [List] of sub-tokenizers to produce actual tokens
 * @property activeTokenizers is a [LinkedList] of sub-tokenizers with [BuildingStatus]
 * different from [BuildingStatus.NONE] or [BuildingStatus.CANCELLED]
 */
open class MultiTokenizer<E: Enum<E>>(
    private val subTokenizers: List<AbstractTokenizer<E>>
) : AbstractTokenizer<E>() {

  private val activeTokenizers: LinkedList<AbstractTokenizer<E>> = LinkedList()


  /**
   * Resets [activeTokenizers] and all [subTokenizers]
   */
  override fun reset() {
    super.reset()
    activeTokenizers.clear()
    subTokenizers.forEach { it.reset() }
  }


  override fun firstChar(char: Char, idx: Int, isLast: Boolean) {
    var isCancelled: Boolean = false

    subTokenizers.forEach { tokenizer: AbstractTokenizer<E> ->
      tokenizer.processChar(char, idx, isLast)
      when (tokenizer.getBuildingStatus()) {
        BuildingStatus.FAILED -> {
          tokenBuilder = tokenizer.tokenBuilder
          return
        }
        BuildingStatus.BUILDING, BuildingStatus.FINISHED -> activeTokenizers.add(tokenizer)
        BuildingStatus.CANCELLED -> { isCancelled = true }
        BuildingStatus.NONE -> {}
      }
    }

    TODO()
  }


  override fun nextChar(char: Char, idx: Int, isLast: Boolean) {
    TODO()
  }
}
