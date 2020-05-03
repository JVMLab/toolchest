package com.jvmlab.commons.parse.text


/**
 * Implements a piece of the [CharSequence] with [start] and [finish] points
 */
open class SubSequence(startParam: Int, finishParam: Int = startParam) : ISubSequence {
  // Checks consistency of the token bounds startParam and finishParam
  init {
    require(startParam >= 0) { "Start ($startParam) cannot be negative" }
    require(startParam <= finishParam) {
      "Start ($startParam) is greater than finish ($finishParam)"
    }
  }

  override val start: Int = startParam
  override val finish: Int = finishParam


  /**
   * Gives [CharSequence] representation of this piece based on a [source] [CharSequence]
   */
  override fun subSequence(source: CharSequence): CharSequence = source.subSequence(start, finish + 1)


  /**
   * Gives length of this [CharSequence] piece in characters
   */
  override fun length(): Int = finish - start + 1
}