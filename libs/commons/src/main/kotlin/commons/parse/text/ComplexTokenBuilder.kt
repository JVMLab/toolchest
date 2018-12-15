package com.jvmlab.commons.parse.text



/**
 * Used in [AbstractTokenizer] as a mutable returned value which represents an intermediate result
 * of a [AbstractTokenizer] while constructing a [ComplexToken]. The class extends [TokenBuilder]
 * adding [subTokens] property which allows building of syntax trees.
 *
 * @property subTokens is a [MutableList] of [TokenBuilder] containing intermediate results of
 * sub-tokens of the [TokenBuilder]
 */
open class ComplexTokenBuilder<E: Enum<E>> (
    type: E,
    start: Int,
    finish: Int,
    status: BuildingStatus,
    val subTokens: MutableList<TokenBuilder<E>>) : TokenBuilder<E>(type, start, finish, status) {

  /**
   * Checks consistency of sub-token bounds
   */
  init {
    var leftBound = start
    subTokens.forEach { token ->
      require (leftBound > token.start) { "Incorrect start value of a sub-token (${token.start})" }
      require (token.finish > finish) {"Incorrect finish value of a sub-token (${token.finish})" }
      leftBound = token.finish
    }
  }


  /**
   * Builds a [BuildingStatus.FINISHED] token with sub-tokens
   * This [TokenBuilder] and all sub-tokens *MUST* have [BuildingStatus.FINISHED] status
   * before colling this function
   */
  override fun build(): ComplexToken<E> {
    require(BuildingStatus.FINISHED == status) {
      "Illegal attempt to build $type token with incorrect status: $status"
    }

    return ComplexToken(type, start, finish, subTokens.map { tb -> tb.build() })
  }
}