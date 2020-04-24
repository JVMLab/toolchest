package com.jvmlab.commons.parse.text


/**
 * Represents an incomplete [Token] in progress and has build() method to build a completed [Token] when
 * [status] equals [StatusFinished]
 * Used in [AbstractTokenizer] to keep an intermediate result of the [AbstractTokenizer]
 * while constructing a [Token]. The class extends [Token] and adds some variable
 * properties to monitor the current status or the result of a [Token] creation
 *
 * @property type is overridden to variable and used as a [Token] type built by this [TokenBuilder]
 * @property start is overridden to variable, should be 0 for [StatusNone]
 * @property finish is overridden to variable, should be 0 for [StatusNone]
 * @property status represents the current status or the result of a [Token] creation
 */
private class TokenBuilder<E: Enum<E>> (
    override var type: E,
    start: Int = 0,
    finish: Int = start,
    status: TokenizerStatus = StatusNone) : Token<E>(type, start, finish) {

  override var start: Int = start
    set(value) {
      if (status == StatusNone) {
        require(value == 0) {
          "Incorrect start value ($value) for the BuildingStatus $StatusNone"
        }
      }
      require(value <= finish) {
        "Illegal attempt to set start value ($value) greater than finish ($finish)"
      }
      field = value
    }

  override var finish: Int = finish
    set(value) {
      if (status == StatusNone) {
        require(value == 0) {
          "Incorrect finish value ($value) for the BuildingStatus $StatusNone"
        }
      }
      require(value >= start) {
        "Illegal attempt to set finish value ($value) less than start ($start)"
      }
      field = value
    }

  var status: TokenizerStatus = status
    set(value) {
      if (status == StatusNone) {
        require(start == 0) {
          "Incorrect start value ($start) for the BuildingStatus $StatusNone"
        }
        require(finish == 0) {
          "Incorrect finish value ($finish) for the BuildingStatus $StatusNone"
        }
      }
      field = value
    }


  /**
   * Resets this [TokenBuilder] to the default values and the initial [type]
   */
  fun reset() {
    type = super.type
    start = 0
    finish = 0
    status = StatusNone
  }


  /**
   * Sets [TokenBuilder] properties to start a new token
   */
  fun startToken(start: Int, status: TokenizerStatus) {
    require(status != StatusNone) {
      "Incorrect status ($status) to start a token"
    }
    this.status = status
    this.finish = start
    this.start = start
  }


  /**
   * Builds a new [Token]
   * A [TokenBuilder] *MUST* have [StatusFinished] before colling this method
   *
   * @throws IllegalStateException when [TokenBuilder] has an improper [TokenizerStatus]
   */
  fun build(): Token<E> {
    check(status == StatusFinished) {
      "Illegal attempt to build $type token with incorrect status: $status"
    }
    return Token<E>(type, start, finish)
  }

}


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
   * returns type of the [tokenBuilder]
   */
  override fun getTokenType(): E = tokenBuilder.type


  /**
   * returns building status of the [tokenBuilder]
   */
  override fun getBuildingStatus(): TokenizerStatus = tokenBuilder.status


  /**
   * returns a current finish of the [tokenBuilder]
   */
  override fun getCurrentFinish() = tokenBuilder.finish


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
   * @throws IllegalStateException when [tokenBuilder] has an improper [TokenizerStatus]
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
   * Possible [TokenizerStatus] values to be set in the returned value:
   *  - [isLast] == false : any value
   *  - [isLast] == true  : any value except [StatusBuilding]
   *
   *
   * @param char is a [Char] to be parsed
   * @param idx is a position of the [char] in a parsed [CharSequence]
   * @param isLast indicates if [idx] is the last index of a parsed [CharSequence]. It is intended
   * to be used by an implementation to set a proper [TokenizerStatus] in case of the last char
   *
   * @return [TokenizerStatus] with the building status and reason
   */
  protected abstract fun firstChar(char: Char, idx: Int, isLast: Boolean): TokenizerStatus


  /**
   * Parses a next [char] of a token
   *
   * The method is called when [getBuildingStatus] returns [StatusBuilding]
   * to prevent parsing of a token with incorrect initial status.
   *
   * Possible [TokenizerStatus] values to be set in the returned value::
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
   * to be used by an implementation to set a proper [TokenizerStatus] of the [tokenBuilder]
   *
   * @return [TokenizerStatus] with the building status and reason
   */
  protected abstract fun nextChar(char: Char, idx: Int, isLast: Boolean): TokenizerStatus

}