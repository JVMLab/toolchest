package com.jvmlab.commons.parse.text


/**
 * Represents a piece of the [CharSequence] with [start] and [finish] points
 *
 * @property start is a starting point in a [CharSequence] (inclusive)
 * @property finish is an ending point in a [CharSequence] (inclusive)
 */
interface ISubSequence {
  val start: Int
  val finish: Int
  fun subSequence(source: CharSequence): CharSequence
  fun length(): Int
}