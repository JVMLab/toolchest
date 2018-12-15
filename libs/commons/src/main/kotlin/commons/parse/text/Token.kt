package com.jvmlab.commons.parse.text



/**
 * Describes an text token taken from a [CharSequence].
 *
 * @property type is a type of this token, to be defined in sub-classes
 * @property start is a starting point in a [CharSequence] (inclusive)
 * @property finish is an ending point in a [CharSequence] (inclusive)
 */
open class Token<E: Enum<E>>(
    val type: E,
    val start: Int,
    open val finish: Int) {

  /**
   * Checks consistency of the token bounds
   */
  init {
    require(start <= finish) { "Start ($start) is greater than finish ($finish)" }
  }

    /**
     * Gives [CharSequence] representation of this token based on a [source] [CharSequence]
     */
    open fun subSequence(source: CharSequence): CharSequence = source.subSequence(start, finish)


    /**
     * Gives [String] representation of this token based on a [source] [CharSequence]
     *
     * @param source is a source [CharSequence] for this token
     * @param indent is an optional prefix for a proper indent while printing a [ComplexToken]
     */
    open fun asString(source: CharSequence, indent: String = ""): String =
        indent + subSequence(source).toString()


    /**
     * Gives length of the token in characters
     */
    open fun length(): Int = finish - start + 1
}