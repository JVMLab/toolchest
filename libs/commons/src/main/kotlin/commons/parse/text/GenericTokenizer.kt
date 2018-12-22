package com.jvmlab.commons.parse.text



/**
 * A [SingleTokenizer] to create tokens based on conditions for chars given by external functions
 *
 * @property checkFirstChar a function to check the first char of a token, to be used in
 * [firstChar] method. If returns true then a new [TokenBuilder] is created
 * @property firstCharStatus is a [BuildingStatus] for the token created by [firstChar]. Usually
 * [BuildingStatus.BUILDING], but could be also [BuildingStatus.FINISHED] for a single-char token
 * @property checkNextChar a function to check a next and the last char of a token, to be used in
 * [nextChar] and [lastChar] methods. If returns true then the [TokenBuilder] is extended to one
 * more char by the [nextChar] or finished successfully by the [lastChar]
 */
open class GenericTokenizer<E: Enum<E>>(
    type: E,
    private val checkFirstChar: (Char) -> Boolean,
    private val firstCharStatus: BuildingStatus = BuildingStatus.BUILDING,
    private val checkNextChar: (Char) -> Boolean = checkFirstChar
) : SingleTokenizer<E>(type) {

  override fun firstChar(char: Char, start: Int): TokenBuilder<E>? =
    if (checkFirstChar(char)) TokenBuilder<E>(type, start, start, firstCharStatus)
    else null


  override fun nextChar(char: Char, tokenBuilder: TokenBuilder<E>) {
    validateTokenStatus(tokenBuilder.status)
    if (checkNextChar(char)) tokenBuilder.finish++
    else tokenBuilder.status = BuildingStatus.FINISHED
  }


  override fun lastChar(char: Char, tokenBuilder: TokenBuilder<E>) {
    validateTokenStatus(tokenBuilder.status)
    check(checkNextChar(char)) {
      "Unexpected state: the last char should always match a $type token"
    }
    tokenBuilder.status = BuildingStatus.FINISHED
  }
}