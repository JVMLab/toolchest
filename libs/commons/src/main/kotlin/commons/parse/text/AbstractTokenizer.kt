package com.jvmlab.commons.parse.text


/**
 * Contains functions to parse [Char]s from a [CharSequence] using mutable [TokenBuilder] to hold
 * an intermediate state of a [ComplexToken] to be parsed
 */
abstract class AbstractTokenizer<E: Enum<E>> {

  /**
   * Parses first [char] of a token.
   *
   * An implementation *SHOULD* always set [TokenBuilder.start] and [TokenBuilder.finish]
   * equal to [start]
   *
   * @param char is a [Char] to be parsed
   * @param start is a position of the [char] in a parsed [CharSequence] to be stored in
   * a returned [TokenBuilder]
   *
   * @return a [TokenBuilder] or null if the [char] is not a [Char] which can start a token
   */
  abstract fun firstChar(char: Char, start: Int): TokenBuilder<E>?


  /**
   * Parses a next [char] of a token.
   *
   * An implementation *MUST* call [validateTokenStatus] to prevent parsing of a token
   * with incorrect initial status
   *
   * @param char is a [Char] to be parsed
   * @param tokenBuilder is a value returned by a previous call to [firstChar] or [nextChar]
   *
   * @return a modified [tokenBuilder]
   */
  abstract fun nextChar(char: Char, tokenBuilder: TokenBuilder<E>)


  /**
   * A function to be used in [nextChar] to validate [status] and throw an exception
   * in case of an illegal status
   */
  protected fun validateTokenStatus(status: BuildingStatus) =
      require(BuildingStatus.BUILDING == status) {
        "Illegal attempt to parse $status token"
      }
}