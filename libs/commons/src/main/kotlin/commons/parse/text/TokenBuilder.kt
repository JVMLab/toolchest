package com.jvmlab.commons.parse.text


/**
 * Represents a current status of a token to be built by a [TokenBuilder]
 */
enum class TokenStatus {
  BUILDING,
  FINISHED,
  FAILED
}


/**
 * Used in [Tokenizer] as a return mutable value which represents an intermediate result of
 * a [Tokenizer] while constructing a [Token]. The interface is parametrized with [Enum]
 * type parameter which defines a possible type of a token produced by the [TokenBuilder]
 *
 * @property type is a type of a token to be built
 * @property start is a starting point in a [CharSequence] (inclusive)
 * @property finish is an ending point in a [CharSequence] (inclusive)
 * @property status is the status of the token to be built
 * @property subTokens is a [MutableList] of [TokenBuilder] containing intermediate results of
 * sub-tokens of the [TokenBuilder]
 */
open class TokenBuilder<E: Enum<E>> (
    val type: E,
    val start: Int,
    var finish: Int,
    var status: TokenStatus,
    val subTokens: MutableList<TokenBuilder<E>>
) {

  /**
   * Builds a [TokenStatus.FINISHED] token with sub-tokens
   * This [TokenBuilder] and all sub-tokens *MUST* have [TokenStatus.FINISHED] status
   * before colling this function
   */
  fun build(): Token<E> {
    if (TokenStatus.FINISHED != status) {
      throw IllegalStateException("Attempt to build incomplete $type token")
    }

    return Token(type, start, finish, subTokens.map { tb ->
      tb.build()
    })
  }
}