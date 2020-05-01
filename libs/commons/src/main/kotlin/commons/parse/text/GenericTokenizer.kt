package com.jvmlab.commons.parse.text


/**
 * A subclass of [GenericSingleCharTokenizer] to create multi-char tokens
 */
open class GenericTokenizer<E: Enum<E>>(
    type: E,
    checkChar: (Char) -> Boolean
) : GenericSingleCharTokenizer<E>(type, checkChar), IProcessTokenizer<E> {

  override fun startProcessing(char: Char, idx: Int): TokenizerStatus =
      if (checkChar(char))
        StatusBuilding(this, idx)
      else
        reset()


  override fun processChar(char: Char, lastStatus: StatusBuilding<E>): TokenizerStatus =
      if (checkChar(char))
        lastStatus.nextBuilding()
      else
        lastStatus.thisFinish()


  override fun processLastChar(char: Char, lastStatus: StatusBuilding<E>): FinalStatus =
      if (checkChar(char))
        lastStatus.nextFinish()
      else
        lastStatus.thisFinish()
}
