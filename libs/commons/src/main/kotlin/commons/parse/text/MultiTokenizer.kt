package com.jvmlab.commons.parse.text


/**
 * Represents an [AbstractTokenizer] which produces tokens of multiple types by delegating
 * the actual work to sub-tokenizers
 *
 * @property subTokenizers is a [Map] of sub-tokenizers to produce actual tokens
 */
open class MultiTokenizer<E: Enum<E>>(
    private val subTokenizers: Map<E, AbstractTokenizer<E>>
) : AbstractTokenizer<E>() {

  override fun firstChar(char: Char, idx: Int, isLast: Boolean) {
    subTokenizers.forEach { type: E, tokenizer: AbstractTokenizer<E> ->

    }
    TODO()
  }


  override fun nextChar(char: Char, idx: Int, isLast: Boolean) {
    TODO()
  }
}
