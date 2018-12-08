package com.jvmlab.commons.parse.text


/**
 * Describes a text token taken from a [source] [CharSequence]. The class is parametrized
 * with [Enum] type parameter which defines possible types of the tokens
 *
 * @property source the source [CharSequence] to obtain the token
 * @property start defines a starting point in a [CharSequence] array (inclusive)
 * @property stop defines an ending point in a [CharSequence] array (inclusive)
 * @property type defines a type of this token
 */
open class Token<E: Enum<E>> (
    val source: CharSequence,
    val start: Int,
    val stop: Int,
    val type: E) {


  /**
   * Gives [CharSequence] representation of this token
   */
  fun subSequence(): CharSequence = source.subSequence(start, stop)


  /**
   * [toString] implementation
   */
  override fun toString(): String = subSequence().toString()


  /**
   * Gives length of the token in characters
   */
  fun length(): Int = stop - start
}