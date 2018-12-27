package com.jvmlab.commons.parse.text


/**
 * Contains functions to parse [Char]s from a [CharSequence] using mutable [tokenBuilder] to hold
 * an intermediate state of a [ComplexToken] to be parsed
 *
 * @property tokenBuilder holds a state of the current token in progress. Used by the public
 * [getBuildingStatus] method and to be used in protected [firstChar] and [nextChar] implementations
 * of the [AbstractTokenizer]
 */
abstract class AbstractTokenizer<E: Enum<E>> {

  var tokenBuilder: TokenBuilder<E>? = null
    protected set


  /**
   * Gives the current [BuildingStatus] of a token in progress or [BuildingStatus.NONE]
   * if there is no current token
   */
  fun getBuildingStatus(): BuildingStatus = tokenBuilder?.status ?: BuildingStatus.NONE


  /**
   * Gives the current [TokenBuilder.finish] value of a token in progress or -1
   * if there is no current token
   */
  fun getCurrentFinish(): Int = tokenBuilder?.finish ?: -1


  /**
   * Resets an [AbstractTokenizer] to an initial state.
   *
   * The standard implementation just sets [tokenBuilder] to null. An implementation in a sub-class
   * may use more complex logic, which is why [reset] is used in [buildToken] and in [processChar]
   */
  open fun reset() { tokenBuilder = null }


  /**
   * Builds a resulting token and resets [tokenBuilder] to null, so a current token can be built
   * only once.
   *
   * The standard implementation just delegates the building to [tokenBuilder] [TokenBuilder.build]
   * function, but implementations in sub-classes may use more complex logic.
   *
   * @throws IllegalStateException when [tokenBuilder] is null or has an improper [BuildingStatus]
   */
  open fun buildToken(): Token<E> {
    val token = tokenBuilder?.build() ?: run {
      throw IllegalStateException(
        "Illegal attempt to build a token with incorrect status: ${getBuildingStatus()}")
    }
    reset()
    return token
  }


  /**
   * Processes a current [Char] from a [CharSequence] delegating the actual work to [firstChar]
   * and [nextChar] methods depending on a value returned by [getBuildingStatus]
   *
   * A calling method should call [buildToken] method once [getBuildingStatus] returned
   * [BuildingStatus.FINISHED] before a next call to this [processChar] function otherwise
   * a protected [tokenBuilder] value will be overridden before the [firstChar] call
   * from [processChar]
   *
   * A possible implementation in a sub-class *MUST* always call [reset] prior to the [firstChar]
   * call to ensure that the [AbstractTokenizer] or its sub-class has an initial state
   *
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]
   */
  fun processChar(char: Char, idx: Int, isLast: Boolean) {
    if (BuildingStatus.BUILDING == getBuildingStatus()) {
      nextChar(char, idx, isLast)
    } else {
      reset()
      firstChar(char, idx, isLast)
    }
  }


  /**
   * Parses the first [char] of a token and creates a new [tokenBuilder]
   *
   * An implementation *MUST* always set [TokenBuilder.start] and [TokenBuilder.finish]
   * equal to [idx] in a new [tokenBuilder]
   *
   * An implementation *SHALL* expect that it's called with an initial state of [AbstractTokenizer]
   * after a call to [reset] and *MUST* set [tokenBuilder] to some non-null [TokenBuilder] value
   *
   * Possible [TokenBuilder.status] values to be set in [tokenBuilder] by an implementation
   * (among values of [BuildingStatus] applicable for a [TokenBuilder]):
   *  - [isLast] == false : any value
   *  - [isLast] == true  : any value except [BuildingStatus.BUILDING]
   *
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence] to be stored in
   * [tokenBuilder]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]. It is expected
   * to be used by an implementation to set a proper [BuildingStatus] of the [tokenBuilder]
   */
  protected abstract fun firstChar(char: Char, idx: Int, isLast: Boolean)


  /**
   * Parses a next [char] of a token and modifies the state of the [tokenBuilder]
   *
   * The method can be called only when [getBuildingStatus] returns [BuildingStatus.BUILDING]
   * to prevent parsing of a token with incorrect initial status.
   *
   * Possible [TokenBuilder.status] values to be set in [tokenBuilder] by an implementation
   * (among values of [BuildingStatus] applicable for a [TokenBuilder]):
   *  - [isLast] == false : any value
   *  - [isLast] == true  : any value except [BuildingStatus.BUILDING]
   *
   * An implementation *SHALL* expect that [char] is a next [Char] from a source [CharSequence]
   * after [TokenBuilder.finish] of the [tokenBuilder], so if this [char] matches the token
   * the implementation *MUST* increment [TokenBuilder.finish] in the [tokenBuilder]
   *
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]. It is expected
   * to be used by an implementation to set a proper [BuildingStatus] of the [tokenBuilder]
   */
  protected abstract fun nextChar(char: Char, idx: Int, isLast: Boolean)

}