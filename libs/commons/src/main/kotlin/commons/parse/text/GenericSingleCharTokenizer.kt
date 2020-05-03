package com.jvmlab.commons.parse.text


/**
 * A generic class to create single-char tokens
 */
open class GenericSingleCharTokenizer<E: Enum<E>>(
    override val type: E,
    protected val checkChar: (Char) -> Boolean
) : IStartTokenizer<E> {

  override fun startProcessing(char: Char, idx: Int): ModifiedStatus = startProcessingLast(char, idx)


  override fun startProcessingLast(char: Char, idx: Int): FinalModifiedStatus =
      if (checkChar(char))
        StatusFinished(this, idx)
      else
        StatusCancelled(this, idx)

}