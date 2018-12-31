package com.jvmlab.commons.parse.text


/**
 * An [AbstractTokenizer] which creates a ComplexToken. subTokens of the [ComplexToken] are created
 * by an internal [AbstractTokenizer] stored in a private [subTokenizer] property.
 *
 * @property subTokenizer
 * @property subTokens
 * @property processInSubTokenizer
 */
abstract class AbstractComplexTokenizer<E: Enum<E>>(
    type: E,
    protected val subTokenizer: AbstractTokenizer<E>
) : AbstractTokenizer<E>(type) {

  protected val subTokens: MutableList<Token<E>> = ArrayList()
  protected var processInSubTokenizer: Boolean = false


  /**
   * Resets [subTokenizer] and clears [subTokens]
   */
  override fun reset() {
    super.reset()
    subTokens.clear()
    subTokenizer.reset()
  }


  override fun firstChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails =
      anyChar(char, idx, isLast, ::firstBeforeSubTokenizer, ::firstAfterSubTokenizer)


  override fun nextChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails =
      anyChar(char, idx, isLast, ::nextBeforeSubTokenizer, ::nextAfterSubTokenizer)


  /**
   * Implements common logic of both [firstChar] and [nextChar] while being called with relevant
   * [beforeSubTokenizer] and [afterSubTokenizer] functional parameters
   */
  private fun anyChar(
      char: Char, idx: Int, isLast: Boolean,
      beforeSubTokenizer: (char: Char, idx: Int, isLast: Boolean) -> BuildingDetails,
      afterSubTokenizer: (char: Char, idx: Int, isLast: Boolean) -> BuildingDetails):
      BuildingDetails {
    processInSubTokenizer = false
    val details = beforeSubTokenizer(char, idx, isLast)
    if (BuildingStatus.BUILDING == details.status && processInSubTokenizer) {
      subTokenizer.processChar(char, idx, isLast)
      val subStatus = subTokenizer.getBuildingStatus()
      if (BuildingStatus.FAILED == subStatus) return subTokenizer.getRTokenBuilder().details
      if (BuildingStatus.FINISHED == subStatus) subTokens.add(subTokenizer.buildToken())
      return afterSubTokenizer(char, idx, isLast)
    }
    return details
  }


  /**
   * Is called in [firstChar] before calling [processChar] of [subTokenizer].
   *
   * The implementation *SHALL* set [processInSubTokenizer] to true if [processChar] of
   * [subTokenizer] has to be called after that.
   *
   * The implementation *SHOULD* meet all requirements for [firstChar] of [AbstractTokenizer]
   */
  protected abstract fun
      firstBeforeSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails


  /**
   * Is called in [firstChar] after calling [processChar] of [subTokenizer].
   * The implementation *MUST* meet all requirements for [firstChar] of [AbstractTokenizer]
   */
  protected abstract fun
      firstAfterSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails


  /**
   * Is called in [nextChar] before calling [processChar] of [subTokenizer].
   *
   * The implementation *SHALL* set [processInSubTokenizer] to true if [processChar] of
   * [subTokenizer] has to be called after that.
   *
   * The implementation *SHOULD* meet all requirements for [nextChar] of [AbstractTokenizer]
   */
  protected abstract fun
      nextBeforeSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails


  /**
   * Is called in [nextChar] before calling [processChar] of [subTokenizer].
   *
   * The implementation *SHALL* set [processInSubTokenizer] to true if [processChar] of
   * [subTokenizer] has to be called after that.
   *
   * The implementation *MUST* meet all requirements for [nextChar] of [AbstractTokenizer]
   * including proper set of [finalCharIncluded] property of [AbstractTokenizer]
   */
  protected abstract fun
      nextAfterSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails
}