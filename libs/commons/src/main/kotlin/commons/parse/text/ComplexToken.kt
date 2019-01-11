package com.jvmlab.commons.parse.text



/**
 * Describes a text token taken from a [CharSequence]. The class extends [Token] adding [subTokens]
 * property which allows building of syntax trees.
 *
 * The the [start] and [finish] values for sub-tokens are supposed to be within the limits of
 * the parent token.
 *
 * The sub-tokens of a token are supposed to be sequential, so [start] value of a next sub-token
 * should be more than [finish] value of a previous sub-token
 *
 * @property subTokens overrides parent's property to make it a [List] of [ComplexToken] entities
 */
open class ComplexToken<E: Enum<E>> (
    type: E,
    start: Int,
    finish: Int,
    val subTokens: List<Token<E>>) : Token<E>(type, start, finish) {

  /**
   * Checks consistency of sub-token bounds
   */
  init {
    var leftBound = start
    subTokens.forEach { token ->
      require(leftBound <= token.start) { "Incorrect start value of a sub-token (${token.start})" }
      require(token.finish <= finish) { "Incorrect finish value of a sub-token (${token.finish})" }
      leftBound = token.finish
    }
  }

  /**
   * Additional constructor to create a [ComplexToken] based on [Token]
   */
  constructor(token: Token<E>, subTokens: List<Token<E>>) : this(
      token.type, token.start, token.finish, subTokens)


  /**
   * Pretty printing with sub-tokens
   */
  override fun prettyPrint(source: CharSequence) {
    super.prettyPrint(source)
    subTokens.forEach { it.prettyPrint(source) }
  }

}