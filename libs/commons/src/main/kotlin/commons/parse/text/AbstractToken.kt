package com.jvmlab.commons.parse.text

import java.lang.IllegalArgumentException


/**
 * Describes an abstract text token taken from a [CharSequence].
 *
 * A token may contain a non-empty list of sub-tokens, which allows building of syntax trees.
 *
 * The the [start] and [finish] values for sub-tokens are supposed to be within the limits of
 * the parent token.
 *
 * The sub-tokens of a token are supposed to be sequential, so [start] value of a next sub-token
 * should be more than [finish] value of a previous sub-token
 *
 * @property start is a starting point in a [CharSequence] (inclusive)
 * @property finish is an ending point in a [CharSequence] (inclusive)
 * @property subTokens is a [List] of [AbstractToken] containing of sub-tokens of the token
 */
abstract class AbstractToken(
    val start: Int,
    val finish: Int,
    open val subTokens: List<AbstractToken>) {

  /**
   * Checks consistency of the token and sub-token bounds
   */
  init {
    if (start > finish) {
      throw IllegalArgumentException("Start ($start) is greater than finish ($finish)")
    }

    var leftBound = start
    subTokens.forEach { token ->
      if (leftBound > token.start) {
        throw IllegalArgumentException("Incorrect start value of a sub-token (${token.start})")
      }
      if (token.finish > finish) {
        throw IllegalArgumentException("Incorrect finish value of a sub-token (${token.finish})")
      }
      leftBound = token.finish
    }
  }
}