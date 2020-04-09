package com.jvmlab.commons.parse.text


/**
 * Contains functions to parse [Char]s from a [CharSequence] using mutable [tokenBuilder] to hold
 * an intermediate state of a token in progress
 *
 * @property tokenBuilder holds a state of the current token in progress.
 * @property finalCharIncluded is updated by [nextChar], see the method description
 */
abstract class AbstractTokenizer<E: Enum<E>> (protected val defaultTokenType: E) : ITokenizer<E> {

  private val tokenBuilder: TokenBuilder<E> = TokenBuilder(defaultTokenType)
  protected var finalCharIncluded: Boolean = false


  /**
   * Gives the current read-only version of [tokenBuilder]
   */
  override fun getRTokenBuilder(): RTokenBuilder<E> = tokenBuilder.current


  /**
   * A convenience method to get the current building status from the [tokenBuilder]
   */
  override fun getBuildingStatus(): BuildingStatus = tokenBuilder.status


  /**
   * Could be used in an [AbstractTokenizer] implementation (from [firstChar] and/or [nextChar]
   * methods) to update [type] of the [tokenBuilder] if required
   */
  protected fun setTokenType(type: E) { tokenBuilder.type = type }


  /**
   * Resets an [AbstractTokenizer] to an initial state.
   *
   * The standard implementation just resets [tokenBuilder]. An implementation in a sub-class
   * may use more complex logic, which is why [reset] is used in [buildToken] and in [processChar]
   */
  override fun reset() {
    setTokenType(defaultTokenType)
    tokenBuilder.reset()
  }


  /**
   * Builds a resulting token and resets [tokenBuilder], so a current token can be built
   * only once.
   *
   * The standard implementation just delegates the building to [tokenBuilder] [TokenBuilder.build]
   * method, but implementations in sub-classes may use more complex logic.
   *
   * @throws IllegalStateException when [tokenBuilder] has an improper [BuildingStatus]
   */
  override fun buildToken(): Token<E> {
    val token = tokenBuilder.build()
    reset()
    return token
  }


  /**
   * Processes a current [Char] from a [CharSequence] delegating the actual work to [firstChar]
   * and [nextChar] methods depending on a value returned by [getBuildingStatus]
   *
   * A calling method should call [buildToken] method once [getBuildingStatus] returned
   * [StatusFinished] before a next call to this [processChar] function otherwise
   * a private [tokenBuilder] value will be overridden before the [firstChar] call
   * from [processChar]
   *
   * The method expects that [char] is a next [Char] from a source [CharSequence]
   * after [TokenBuilder.finish] of the [tokenBuilder], so if this [char] matches the token
   * then [TokenBuilder.finish] of the [tokenBuilder] will be incremented in the following cases:
   *
   * - [getBuildingStatus] is [StatusBuilding] before and after call to [processChar]
   * - [getBuildingStatus] is [StatusBuilding] before call to [processChar] and
   * [finalCharIncluded] is set to true by [nextChar]
   *
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]
   */
  override fun processChar(char: Char, idx: Int, isLast: Boolean) {
    if (StatusBuilding == getBuildingStatus()) {
      finalCharIncluded = false
      tokenBuilder.status = nextChar(char, idx, isLast)
      if (finalCharIncluded || StatusBuilding == getBuildingStatus()) tokenBuilder.finish++
    } else {
      reset()
      val status = firstChar(char, idx, isLast)
      if (status != StatusNone)
        tokenBuilder.startToken(idx, status)
    }

    if (isLast) require(tokenBuilder.status != StatusBuilding) {
      "Unexpected $StatusBuilding for the last char at position $idx"
    }
  }


  /**
   * Parses the first [char] of a token
   *
   * An implementation *SHALL* expect that it's called with an initial state of [AbstractTokenizer]
   * after a call to [reset]
   *
   * Possible [BuildingStatus] values to be set in the returned value:
   *  - [isLast] == false : any value
   *  - [isLast] == true  : any value except [StatusBuilding]
   *
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]. It is intended
   * to be used by an implementation to set a proper [BuildingStatus] in case of the last char
   *
   * @return [BuildingStatus] with the building status and reason
   */
  protected abstract fun firstChar(char: Char, idx: Int, isLast: Boolean): BuildingStatus


  /**
   * Parses a next [char] of a token
   *
   * The method is called when [getBuildingStatus] returns [StatusBuilding]
   * to prevent parsing of a token with incorrect initial status.
   *
   * Possible [BuildingStatus] values to be set in the returned value::
   *  - [isLast] == false : any value
   *  - [isLast] == true  : any value except [StatusBuilding]
   *
   * The implementation *MUST* set [finalCharIncluded] to true if [TokenBuilder.finish]
   * of the [tokenBuilder] should be incremented by [processChar] for any of the final statuses
   * (all statuses except [StatusBuilding]), although there is no harm to set
   * [finalCharIncluded] to true even for [StatusBuilding]
   *
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]. It is expected
   * to be used by an implementation to set a proper [BuildingStatus] of the [tokenBuilder]
   *
   * @return [BuildingStatus] with the building status and reason
   */
  protected abstract fun nextChar(char: Char, idx: Int, isLast: Boolean): BuildingStatus

}