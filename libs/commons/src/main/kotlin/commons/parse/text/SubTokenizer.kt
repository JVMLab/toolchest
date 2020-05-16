package com.jvmlab.commons.parse.text


/**
 * A helper data class to keep sub-tokenizers of a [ComplexTokenizer]
 *
 * @property tokenizer is an [IStartTokenizer] to keep
 * @property minTokens is a minimum number of tokens expected from [tokenizer]
 * @property maxTokens is a maximum number of tokens allowed for [tokenizer], 0 means unlimited
 */
data class SubTokenizer<E: Enum<E>>(
    val tokenizer: IStartTokenizer<E>,
    val minTokens: Int = 0,
    val maxTokens: Int = 0) : IStartTokenizer<E> by tokenizer {

  init {
    require(minTokens >= 0) { "minTokens ($minTokens) cannot be negative" }
    require(maxTokens >= 0) { "minTokens ($maxTokens) cannot be negative" }
    require(minTokens <= maxTokens || maxTokens == 0) {
      "minTokens ($minTokens) should be less or equal to maxTokens ($maxTokens)"
    }
  }
}