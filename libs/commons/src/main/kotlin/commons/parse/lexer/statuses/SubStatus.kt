package com.jvmlab.commons.parse.lexer.statuses

import com.jvmlab.commons.parse.lexer.tokenizers.SubTokenizer


/**
 * A [TokenizerStatus] returned by an [SubTokenizer] with some extra metadata about it
 *
 * @property subTokenizers is the list of sub-tokenizers a of [ComplexTokenizer]
 * @property idx the index in [subTokenizers] of the current sub-tokenizer
 * @property status status of the sub-tokenizer
 * @property tokenCount number of tokens created by the sub-tokenizer
 */
class SubStatus<E: Enum<E>> private constructor(
    private val subTokenizers: List<SubTokenizer<E>>,
    var status: TokenizerStatus,
    var idx: Int = 0,
    var tokenCount: Int = 0
) : List<SubTokenizer<E>> by subTokenizers {

  companion object {
    fun <E: Enum<E>> new(subTokenizers: List<SubTokenizer<E>>) =
        SubStatus(subTokenizers, subTokenizers.first().reset())
  }

  private fun subTokenizer() = subTokenizers[idx]
  private fun minTokens() = subTokenizer().minTokens
  private fun maxTokens() = subTokenizer().maxTokens


  fun reset(): SubStatus<E> {
    status = subTokenizer().reset()
    return this
  }


  fun next(): SubStatus<E> {
    idx++
    status = subTokenizer().reset()
    tokenCount = 0
    return this
  }


  /**
   * Returns true if we can finish [ComplexTokenizer] because all required sub-tokens have been created
   */
  fun canFinish() = (tokenCount >= minTokens()) && (
      (idx == lastIndex) ||
          !slice((idx + 1)..lastIndex).any {
            it.minTokens > 0
          }
      )


  fun canCreateMoreTokens() = (maxTokens() == 0) || (tokenCount < maxTokens())


  /**
   * Returns true if we can take the next sub-tokenizer because all required sub-tokens
   * have been created by the current sub-tokenizer
   */
  fun canTakeNext() = (tokenCount >= minTokens()) && (idx < lastIndex)

}
