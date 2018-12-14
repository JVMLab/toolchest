package com.jvmlab.commons.parse.text

import java.lang.IllegalArgumentException



/**
 * Describes a text token taken from a [CharSequence]. The class extends [AbstractToken] and
 * is parametrized with [Enum] type parameter which defines possible type of the token.
 *
 * @property type is a type of this token
 * @property subTokens overrides parent's property to make it a [List] of [Token] entities
 */
open class Token<E: Enum<E>> (
    val type: E,
    start: Int,
    finish: Int,
    override val subTokens: List<Token<E>>) : AbstractToken(start, finish, subTokens) {


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