package com.jvmlab.commons.parse.text



/**
 * An [AbstractTokenizer] to create tokens based on conditions for chars given by external functions
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
) : AbstractTokenizer<E>(type) {

  override fun firstChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails =
    if (checkFirstChar(char))
      if (isLast) BuildingDetails(BuildingStatus.FINISHED)
      else BuildingDetails(firstCharStatus)
    else BuildingDetails()


  override fun nextChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails =
    if (checkNextChar(char)) {
      nextCharIncluded = true
      if (isLast) BuildingDetails(BuildingStatus.FINISHED)
      else getRTokenBuilder().details // expected to have BuildingStatus.BUILDING
    }
    else BuildingDetails(BuildingStatus.FINISHED)

}