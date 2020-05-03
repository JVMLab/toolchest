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
}