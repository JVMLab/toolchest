package com.jvmlab.commons.parse.text


/**
 * A generic class to create single-char tokens
 */
open class GenericSingleCharTokenizer<E: Enum<E>>(
    override val type: E,
    protected val checkChar: (Char) -> Boolean
) : IStartTokenizer<E> {

  override fun startProcessing(char: Char, idx: Int): TokenizerStatus = startProcessingLast(char, idx)


  override fun startProcessingLast(char: Char, idx: Int): FinalStatus =
      if (checkChar(char))
        StatusFinished(this, idx)
      else
        reset()

}