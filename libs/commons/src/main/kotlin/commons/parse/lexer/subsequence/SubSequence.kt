package com.jvmlab.commons.parse.lexer.subsequence


/**
 * Immutable implementation of [ISubSequence]
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


  override fun stretch(extent: Int): ISubSequence {
    require(extent >= 0) { "Extent for stretch() ($extent) cannot be negative" }
    return SubSequence(start, finish + extent)
  }

  override fun stretch(): ISubSequence {
    return SubSequence(start, finish + 1)
  }
}
