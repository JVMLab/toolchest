package com.jvmlab.commons.parse.text


/**
 * A [SubSequence] which can be stretched to some extent
 */
class StretchableSubSequence(startParam: Int, finishParam: Int = startParam) :
    SubSequence(startParam, finishParam),
    IStretchableSubSequence {

  override var finish: Int = finishParam
    private set

  override fun stretch(extent: Int) {
    super.stretch(extent)  // to validate function argument
    finish += extent
  }

  override fun stretch() {
    finish++
  }
}