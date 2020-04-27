package com.jvmlab.commons.parse.text


/**
 * TODO
 */
open class GenericSingleCharTokenizer<E: Enum<E>>(
    type: E,
    private val checkChar: (Char) -> Boolean
) : AbstractTokenizer<E>(type) {

  override fun startProcessing(char: Char, idx: Int): TokenizerStatus = startProcessingLast(char, idx)


  override fun startProcessingLast(char: Char, idx: Int): FinalStatus =
      if (checkChar(char))
        StatusFinished(this, idx)
      else
        reset()


  /**
   * This function is not expected to be called because [startProcessing] will never return [StatusBuilding]
   */
  override fun processChar(char: Char, lastStatus: StatusBuilding<E>): TokenizerStatus = reset()


  /**
   * This function is not expected to be called because [startProcessing] will never return [StatusBuilding]
   */
  override fun processLastChar(char: Char, lastStatus: StatusBuilding<E>): FinalStatus = reset()
}