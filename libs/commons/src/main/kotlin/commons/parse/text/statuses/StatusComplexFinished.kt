package com.jvmlab.commons.parse.text.statuses


import com.jvmlab.commons.parse.text.*
import com.jvmlab.commons.parse.text.subsequence.*
import com.jvmlab.commons.parse.text.tokenizers.*

/**
 * Subclass of [StatusFinished] used by [StatusComplexBuilding]. It creates [ComplexToken]
 */
class StatusComplexFinished<E: Enum<E>>(
    tokenizer: IResetTokenizer<E>,
    subSequence: ISubSequence,
    private val subTokens: List<Token<E>>) : StatusFinished<E>(tokenizer, subSequence) {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      subTokens: List<Token<E>>
  ) : this(tokenizer, SubSequence(start, start), subTokens)


  override fun createToken(): Token<E> = ComplexToken(super.createToken(), subTokens)
}
