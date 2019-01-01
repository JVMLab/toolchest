package com.jvmlab.commons.parse.text


/**
 * Creates [ComplexToken] for brackets and their internal content as sub-tokens
 *
 * @property leftBracket represents a left bracket [Char]
 * @property rightBracket represents a right bracket [Char]
 * @property whiteSpaceAllowed indicates if whitespaces which are not part of a sub-token
 * should be ignored (true) or reported as a token failure (false)
 * @property maxNumberOfSubTokens maximum allowed number of sub-tokens expected from [subTokenizer],
 * any negative value is treated as unlimited
 */
class BracketsTokenizer<E: Enum<E>>(
    type: E,
    subTokenizer: AbstractTokenizer<E>,
    private val leftBracket: Char,
    private val rightBracket: Char,
    private val whiteSpaceAllowed: Boolean = true,
    private val maxNumberOfSubTokens: Int = -1
) : AbstractComplexTokenizer<E>(type, subTokenizer) {


  override fun firstBeforeSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    processInSubTokenizer = false
    if (char == leftBracket)
      return (
          if (isLast) BuildingDetails(BuildingStatus.FAILED,
              "Unexpected $leftBracket as the last char at position $idx")
          else BuildingDetails(BuildingStatus.BUILDING)
          )

    return BuildingDetails()
  }


  override fun firstAfterSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    error("Unexpected call of firstAfterSubTokenizer() method")
  }


  override fun nextBeforeSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    TODO("not implemented") // File | Settings | Editor | File and Code Templates.
  }


  override fun nextAfterSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    TODO("not implemented") // File | Settings | Editor | File and Code Templates.
  }
}