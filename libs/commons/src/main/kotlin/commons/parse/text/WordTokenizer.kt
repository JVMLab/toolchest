package com.jvmlab.commons.parse.text



/**
 * A [SingleTokenizer] to create word tokens with [Char]s matching [Char.isLetterOrDigit] condition
 */
class WordTokenizer<E: Enum<E>>(type: E) : SingleTokenizer<E>(type) {

  override fun firstChar(char: Char, start: Int): TokenBuilder<E>? =
    if (char.isLetterOrDigit())
      TokenBuilder<E>(type, start, start, BuildingStatus.BUILDING)
    else null


  override fun nextChar(char: Char, tokenBuilder: TokenBuilder<E>) {
    validateTokenStatus(tokenBuilder.status)

    if (char.isLetterOrDigit()) {
      tokenBuilder.finish++
    } else {
      tokenBuilder.status = BuildingStatus.FINISHED
    }
  }
}