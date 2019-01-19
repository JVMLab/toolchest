package com.jvmlab.commons.parse.text



/**
 * Describes an text token taken from a [CharSequence].
 *
 * @property type is a type of this token, to be defined in sub-classes
 * @property start is a starting point in a [CharSequence] (inclusive)
 * @property finish is an ending point in a [CharSequence] (inclusive)
 */
open class Token<E: Enum<E>>(open val type: E, startParam: Int, finishParam: Int) {

  // Checks consistency of the token bounds startParam and finishParam
  init {
    require(startParam >= 0) { "Start ($startParam) cannot be negative" }
    require(startParam <= finishParam) {
      "Start ($startParam) is greater than finish ($finishParam)"
    }
  }

  open val start: Int = startParam
  open val finish: Int = finishParam


  /**
   * Gives [CharSequence] representation of this token based on a [source] [CharSequence]
   */
  open fun subSequence(source: CharSequence): CharSequence = source.subSequence(start, finish + 1)


  /**
   * Pretty printing of this token based on a [source] [CharSequence]
   *
   * @param source is a source [CharSequence] for this token
   */
  open fun prettyPrint(source: CharSequence) =
      println("$type : " + " ".repeat(start) + "'${subSequence(source).toString()}'")


  /**
   * Gives length of the token in characters
   */
  open fun length(): Int = finish - start + 1


  override fun toString(): String {
    return "Token(type=$type, start=$start, finish=$finish)"
  }
}