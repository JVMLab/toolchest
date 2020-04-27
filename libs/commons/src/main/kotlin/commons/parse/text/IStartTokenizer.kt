package com.jvmlab.commons.parse.text


/**
 * Contains functions to start a new tokenization.
 */
interface IStartTokenizer<E: Enum<E>> : ITokenizerType<E> {
  /**
   * Starts a new tokenization. Resets [ITokenizer] to an initial state before processing a [Char].
   * For a last [Char] from a [CharSequence] the [startProcessingLast] should be called instead.
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   */
  fun startProcessing(char: Char, idx: Int) : TokenizerStatus


  /**
   * Starts a new tokenization for the last [Char] of a [CharSequence]. Resets [ITokenizer] to an initial
   * state before processing a [Char]. It returns [FinalStatus] because tokenization must be
   * finished by the end of the [CharSequence].
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   */
  fun startProcessingLast(char: Char, idx: Int) : FinalStatus
}