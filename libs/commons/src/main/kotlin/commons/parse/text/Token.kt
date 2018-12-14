package com.jvmlab.commons.parse.text

import java.lang.IllegalArgumentException



/**
 * Describes a text token taken from a [CharSequence]. The class is parametrized
 * with [Enum] type parameter which defines possible type of the token.
 *
 * A token may contain a non-empty list of sub-tokens, which allows building of syntax trees.
 *
 * The the [start] and [finish] values for sub-tokens are supposed to be within the limits of
 * the parent token.
 *
 * The sub-tokens of a token are supposed to be sequential, so [start] value of a next sub-token
 * should be more than [finish] value of a previous sub-token
 *
 * @property type defines a type of this token
 * @property start defines a starting point in a [CharSequence] (inclusive)
 * @property finish defines an ending point in a [CharSequence] (inclusive)
 * @property subTokens is a [List] of [Token] containing of sub-tokens of the [Token]
 */
open class Token<E: Enum<E>> (
    val type: E,
    val start: Int,
    val finish: Int,
    val subTokens: List<Token<E>>) {

  /**
   * Checks consistency of the token and sub-token bounds
   */
  init {
    if (start > finish) {
      throw IllegalArgumentException("Start ($start) is greater than finish ($finish) for $type")
    }

    var leftBound = start
    subTokens.forEach { token ->
      if ((leftBound > token.start) or (token.finish > finish)) {
        throw IllegalArgumentException("Incorrect bound of ${token.type} from the $type token")
      }
      leftBound = token.finish
    }
  }

  /**
   * Gives [CharSequence] representation of this token based on a [source] [CharSequence]
   */
  open fun subSequence(source: CharSequence): CharSequence = source.subSequence(start, finish)


  /**
   * Gives [String] representation of this token based on a [source] [CharSequence]
   */
  open fun asString(source: CharSequence): String = subSequence(source).toString()


  /**
   * Gives length of the token in characters
   */
  open fun length(): Int = finish - start + 1
}