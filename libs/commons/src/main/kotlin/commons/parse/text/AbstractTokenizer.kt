package com.jvmlab.commons.parse.text

import java.lang.IllegalArgumentException


/**
 * Contains functions to parse [Char]s from a [CharSequence] using mutable [TokenBuilder] to hold
 * an intermediate state of a [Token] to be parsed
 */
abstract class AbstractTokenizer<E: Enum<E>> {

  /**
   * Parses first [char] of a token
   *
   * @param char is a [Char] to be parsed
   * @param start is a position of the [char] in a parsed [CharSequence] to be stored in
   * a returned [TokenBuilder]
   *
   * @return a [TokenBuilder] or null if the [char] is not a [Char] which can start a token
   */
  abstract fun firstChar(char: Char, start: Int): TokenBuilder<E>?


  /**
   * Parses a next [char] of a token. An implementation *MUST* call [validateTokenStatus]
   * to prevent parsing of a token with incorrect initial status
   *
   * @param char is a [Char] to be parsed
   * @param tokenBuilder is a value returned by a previous call to [firstChar] or [nextChar]
   *
   * @return a modified [tokenBuilder]
   */
  abstract fun nextChar(char: Char, tokenBuilder: TokenBuilder<E>): TokenBuilder<E>


  /**
   * A function to be used in [nextChar] to validate [tokenStatus] and throw an exception
   * in case of an illegal status
   */
  protected fun validateTokenStatus(tokenStatus: TokenStatus) =
      require(TokenStatus.BUILDING == tokenStatus) {
        "An illegal attempt to parse $tokenStatus token"
      }
}