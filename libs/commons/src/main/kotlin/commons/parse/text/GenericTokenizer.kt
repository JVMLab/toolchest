package com.jvmlab.commons.parse.text



/**
 * A [SingleTokenizer] to create tokens based on conditions for chars given by external functions
 *
 *
 * @property checkFirstChar a function to check the first char of a token, to be used in
 * [firstChar] method. If returns true then a new [TokenBuilder] is created
 *
 * @property firstCharStatus is a [BuildingStatus] for the token created by the [firstChar] method.
 * Usually it is [BuildingStatus.BUILDING], but could be also [BuildingStatus.FINISHED] in case of
 * a single-char token
 *
 * @property checkNextChar a function to check a next and the last char of a token, to be used in
 * [nextChar] method. If returns true then the [TokenBuilder] is extended to one
 * more char by the [nextChar] or finished successfully in case of the last [Char] of a parsed
 * [CharSequence]
 */
open class GenericTokenizer<E: Enum<E>>(
    type: E,
    private val checkFirstChar: (Char) -> Boolean,
    private val firstCharStatus: BuildingStatus = BuildingStatus.BUILDING,
    private val checkNextChar: (Char) -> Boolean = checkFirstChar
) : SingleTokenizer<E>(type) {

  override fun firstChar(char: Char, idx: Int, isLast: Boolean) {
    if (checkFirstChar(char))
      tokenBuilder =
          if (isLast)
            TokenBuilder<E>(type, idx, status = BuildingStatus.FINISHED)
          else
            TokenBuilder<E>(type, idx, status = firstCharStatus)
  }

  override fun nextChar(char: Char, idx: Int, isLast: Boolean) {
    if (checkNextChar(char)) {
      if (isLast) tokenBuilder?.status = BuildingStatus.FINISHED
      tokenBuilder?.apply { finish++ }
    }
    else tokenBuilder?.status = BuildingStatus.FINISHED
  }
}