package com.jvmlab.commons.parse.text

/**
 * An [ISubSequence] which can be stretched to some extent
 */
interface IStretchableSubSequence : ISubSequence {

  /**
   * Argument validation only, an actual extension should be provided by implementation
   */
  fun stretch(extent : Int) {
    require(extent >= 0) { "Extent for stretch() ($extent) cannot be negative" }
  }

  /**
   * No-argument version to improve performance
   */
  fun stretch()
}