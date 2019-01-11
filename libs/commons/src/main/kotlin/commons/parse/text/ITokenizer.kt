package com.jvmlab.commons.parse.text


/**
 * Contains common public functions of all tokenizers
 */
interface ITokenizer<E: Enum<E>> {

  /**
   * Gives the current read-only version of internal [TokenBuilder]
   */
  fun getRTokenBuilder(): RTokenBuilder<E>


  /**
   * A convenience method to get the current building status from the internal [TokenBuilder]
   */
  fun getBuildingStatus(): BuildingStatus


  /**
   * Resets an [ITokenizer] to an initial state.
   */
  fun reset()


  /**
   * Builds a resulting token and resets the internal [TokenBuilder], so a current token can be
   * built only once
   */
  fun buildToken(): Token<E>


  /**
   * Processes a current [Char] from a [CharSequence]
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]
   */
  fun processChar(char: Char, idx: Int, isLast: Boolean)
}