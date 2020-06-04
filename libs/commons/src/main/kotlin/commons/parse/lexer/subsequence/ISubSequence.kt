package com.jvmlab.commons.parse.lexer.subsequence


/**
 * Represents a piece of the [CharSequence] with [start] and [finish] points
 *
 * @property start is a starting point in a [CharSequence] (inclusive)
 * @property finish is an ending point in a [CharSequence] (inclusive)
 */
interface ISubSequence {
  val start: Int
  val finish: Int

  /**
   * Gives [CharSequence] representation of this [ISubSequence] based on [source]
   */
  fun sequence(source: CharSequence): CharSequence = source.subSequence(start, finish + 1)


  /**
   * Gives length of this [ISubSequence] in characters
   */
  fun length(): Int = finish - start + 1


  /**
   * Returns [ISubSequence] with [finish] increased by [extent], which should not be negative
   */
  fun stretch(extent : Int) : ISubSequence


  /**
   * Returns [ISubSequence] with [finish] increased by 1
   */
  fun stretch() : ISubSequence
}
