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


  /**
   * This function is not expected to be called because [startProcessing] will never return [StatusBuilding]
   */
/*  override fun processChar(char: Char, lastStatus: StatusBuilding<E>): TokenizerStatus = reset()


  *//**
   * This function is not expected to be called because [startProcessing] will never return [StatusBuilding]
   *//*
  override fun processLastChar(char: Char, lastStatus: StatusBuilding<E>): FinalStatus = reset()*/
}