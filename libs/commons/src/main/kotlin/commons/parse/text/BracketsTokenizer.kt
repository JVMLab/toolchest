package com.jvmlab.commons.parse.text


/**
 * Creates [ComplexToken] for brackets and their internal content as sub-tokens
 *
 * @property leftBracket represents a left bracket [Char]
 * @property rightBracket represents a right bracket [Char]
 */
class BracketsTokenizer<E: Enum<E>>(
    type: E,
    subTokenizer: AbstractTokenizer<E>,
    private val leftBracket: Char,
    private val rightBracket: Char
) : AbstractComplexTokenizer<E>(type, subTokenizer) {


  override fun firstBeforeSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    TODO("not implemented") // File | Settings | Editor | File and Code Templates.
  }


  override fun firstAfterSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    TODO("not implemented") // File | Settings | Editor | File and Code Templates.
  }


  override fun nextBeforeSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    TODO("not implemented") // File | Settings | Editor | File and Code Templates.
  }


  override fun nextAfterSubTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    TODO("not implemented") // File | Settings | Editor | File and Code Templates.
  }
}