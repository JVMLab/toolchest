package com.jvmlab.commons.parse.text


/**
 * Contains functions to process a next [Char] from a [CharSequence]
 */
interface IProcessTokenizer<E: Enum<E>> : IResetTokenizer<E> {
  /**
   * Processes a next [Char] from a [CharSequence] except a last [Char], see [processLastChar].
   * The processing
   *
   * @param char is a [Char] to be parsed
   */
  fun processChar(char: Char, lastStatus: StatusBuilding<E>) : TokenizerStatus


  /**
   * Processes a last [Char] from a [CharSequence]. It returns [FinalStatus] because tokenization must be
   * finished by the end of the [CharSequence].
   *
   * @param char is a [Char] to be parsed
   */
  fun processLastChar(char: Char, lastStatus: StatusBuilding<E>) : FinalStatus
}