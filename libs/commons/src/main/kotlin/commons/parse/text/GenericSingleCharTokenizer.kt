package com.jvmlab.commons.parse.text


/**
 * A generic class to create single-char tokens
 */
open class GenericSingleCharTokenizer<E: Enum<E>>(
    override val type: E,
    protected val checkChar: (Char) -> Boolean
) : IStartTokenizer<E> {

  override fun startProcessing(char: Char, start: Int): ModifiedStatus = startProcessingLast(char, start)


  override fun startProcessingLast(char: Char, start: Int): FinalModifiedStatus =
      if (checkChar(char))
        StatusFinished(this, start)
      else
        StatusCancelled(this, start)

}