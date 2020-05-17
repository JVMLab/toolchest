package com.jvmlab.commons.parse.text.subsequence


/**
 * A [SubSequence] which can increment [finish] and return itself in [stretch] functions
 */
class VariableSubSequence(startParam: Int, finishParam: Int = startParam) :
    SubSequence(startParam, finishParam) {

  override var finish: Int = finishParam
    private set

  override fun stretch(extent: Int): ISubSequence {
    require(extent >= 0) { "Extent for stretch() ($extent) cannot be negative" }
    finish += extent
    return this
  }

  override fun stretch(): ISubSequence {
    finish++
    return this
  }
}
