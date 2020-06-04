package com.jvmlab.commons.parse.lexer

import com.jvmlab.commons.parse.lexer.subsequence.*


/**
 * Describes an text token taken from a [CharSequence].
 *
 * @property type is a type of this token, to be defined in sub-classes
 */
open class Token<E: Enum<E>> protected constructor(
    val type: E,
    private val subSequence: ISubSequence) : ISubSequence by subSequence {

  constructor(type: E, start: Int, finish: Int = start) : this(type, SubSequence(start, finish))

  /**
   * Pretty printing of this token based on a [source] [CharSequence]
   *
   * @param source is a source [CharSequence] for this token
   */
  open fun prettyPrint(source: CharSequence) = println("$type : " + " ".repeat(start) + "'${sequence(source)}'")

  override fun toString(): String = "Token(type=$type, start=$start, finish=$finish)"
}