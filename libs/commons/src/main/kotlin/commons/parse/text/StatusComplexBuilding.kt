package com.jvmlab.commons.parse.text


/**
 * Subclass of [StatusBuilding] to keep state of [ComplexTokenizer]
 */
class StatusComplexBuilding<E: Enum<E>> private constructor(
    tokenizer: IProcessTokenizer<E>,
    subSequence: ISubSequence,
    var currentSubTokenizer: SubTokenizer<E>,
    private val subTokens: MutableList<Token<E>>
) : StatusBuilding<E>(tokenizer, subSequence),
    MutableList<Token<E>> by subTokens {

  constructor(
      tokenizer: IProcessTokenizer<E>,
      start: Int,
      currentSubTokenizer: SubTokenizer<E>,
      subTokens: MutableList<Token<E>>
  ) : this(tokenizer, VariableSubSequence(start), currentSubTokenizer, subTokens)


  override fun nextFinish() : StatusFinished<E> =
      StatusComplexFinished(tokenizer, subSequence.stretch(), subTokens)

  override fun thisFinish(): StatusFinished<E> =
      StatusComplexFinished(tokenizer, subSequence, subTokens)
}