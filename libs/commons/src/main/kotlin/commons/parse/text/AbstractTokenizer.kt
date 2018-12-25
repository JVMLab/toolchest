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

  protected var tokenBuilder: TokenBuilder<E>? = null


  /**
   * Gives the current [BuildingStatus] of the [AbstractTokenizer]
   */
  open fun getBuildingStatus(): BuildingStatus = tokenBuilder?.status ?: BuildingStatus.NONE


  /**
   * Parses first [char] of a token.
   *
   * An implementation *SHOULD* always set [TokenBuilder.start] and [TokenBuilder.finish]
   * equal to [start] if a new [TokenBuilder] is created
   *
   * Possible [TokenBuilder.status] values to be set by an implementation:
   *  - [isLast] == false : any value
   *  - [isLast] == true  : any value except [BuildingStatus.BUILDING]
   *
   *
   * @param char is a [Char] to be parsed
   *
   * @param start is a position of the [char] in a parsed [CharSequence] to be stored in
   * a returned [TokenBuilder]
   *
   * @param isLast indicates if [char] is the last [Char] in a parsed [CharSequence]. It is expected
   * to be used in an implementation to set either [BuildingStatus.BUILDING] or
   * another completed [BuildingStatus] in a returned [TokenBuilder]
   *
   * @return a [TokenBuilder] or null if the [char] is not a [Char] which can start a token
   */
  abstract fun firstChar(char: Char, start: Int, isLast: Boolean): TokenBuilder<E>?


  /**
   * Parses a next [char] of a token and modifies the state of the [tokenBuilder]
   *
   * An implementation *MUST* call [validateTokenStatus] to prevent parsing of a token
   * with incorrect initial status.
   *
   * Possible [TokenBuilder.status] values to be set by an implementation:
   *  - [isLast] == false : any value
   *  - [isLast] == true  : any value except [BuildingStatus.BUILDING]
   *
   * An implementation *SHALL* expect that [char] is a next [Char] from a source [CharSequence]
   * after [TokenBuilder.finish] of the [tokenBuilder], so if this [char] matches the token
   * the implementation *MUST* increment [TokenBuilder.finish] in the [tokenBuilder]
   *
   *
   * @param char is a [Char] to be parsed
   *
   * @param tokenBuilder is a value returned by a previous call to [firstChar] or [nextChar]
   *
   * @param isLast indicates if [char] is the last [Char] in a parsed [CharSequence]. It is expected
   * to be used in an implementation to set either [BuildingStatus.BUILDING] or
   * another completed [BuildingStatus] in the [tokenBuilder]
   */
  abstract fun nextChar(char: Char, tokenBuilder: TokenBuilder<E>, isLast: Boolean)


  /**
   * A function to be used in [nextChar] to validate [status] and throw an exception
   * in case of an illegal status
   */
  protected fun validateTokenStatus(status: BuildingStatus) =
      require(BuildingStatus.BUILDING == status) {
        "Illegal attempt to parse $status token"
      }
}