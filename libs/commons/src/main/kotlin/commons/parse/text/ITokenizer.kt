package com.jvmlab.commons.parse.text


/**
 * Contains common public functions of all tokenizers
 */
interface ITokenizer<E: Enum<E>> {

  /**
   * returns type of the internal [TokenBuilder]
   */
  fun getTokenType(): E


  /**
   * returns building status of the internal [TokenBuilder]
   */
  fun getBuildingStatus(): BuildingStatus


  /**
   * returns building status of the internal [TokenBuilder]
   */
  fun getCurrentFinish(): Int


  /**
   * Resets an [ITokenizer] to an initial state.
   */
  fun reset()


  /**
   * Builds a resulting token in [StatusFinished] status and resets [ITokenizer],
   * so a current token can be built only once
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