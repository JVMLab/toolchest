package com.jvmlab.commons.parse.text.statuses

import com.jvmlab.commons.parse.text.Token
import com.jvmlab.commons.parse.text.subsequence.*
import com.jvmlab.commons.parse.text.tokenizers.*

/**
 * Subclass of [StatusBuilding] to keep state of [ComplexTokenizer]
 *
 * @param tokenizer supposed to be a [ComplexTokenizer]
 * @property subStatus a [TokenizerStatus] returned by the current [SubTokenizer]
 * with some extra metadata about it
 * @property subTokens list of sub-tokens generated by all sub-tokenizers so far
 */
class StatusComplexBuilding<E: Enum<E>> private constructor(
    tokenizer: IProcessTokenizer<E>,
    subSequence: ISubSequence,
    val subStatus: SubStatus<E>,
    private val subTokens: MutableList<Token<E>>
) : StatusBuilding<E>(tokenizer, subSequence),
    MutableList<Token<E>> by subTokens {

  constructor(
      tokenizer: IProcessTokenizer<E>,
      start: Int,
      subStatus: SubStatus<E>,
      subTokens: MutableList<Token<E>>
  ) : this(tokenizer, VariableSubSequence(start), subStatus, subTokens)


  override fun nextFinish(): StatusFinished<E> =
      StatusComplexFinished(tokenizer, subSequence.stretch(), subTokens)

  override fun thisFinish(): StatusFinished<E> =
      StatusComplexFinished(tokenizer, subSequence, subTokens)

  fun rightFinish(status: FinalModifiedStatus): StatusFinished<E> =
      if (status.finish == finish)
        thisFinish()
      else
        nextFinish()

  fun rightCancelled(status: FinalModifiedStatus): StatusCancelled<E> =
      if (status.finish == finish)
        thisCancelled()
      else
        nextCancelled()

}
