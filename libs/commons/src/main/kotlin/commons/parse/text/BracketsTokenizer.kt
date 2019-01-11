package com.jvmlab.commons.parse.text


/**
 * Creates [ComplexToken] for brackets and their internal content as sub-tokens
 *
 * @param leftBracket represents a left bracket [Char]
 * @param rightBracket represents a right bracket [Char]
 */
class BracketsTokenizer<E: Enum<E>>(
    type: E,
    subTokenizer: SubTokenizer<E>,
    leftBracketType: E,
    rightBracketType: E,
    leftBracket: Char = '[',
    rightBracket: Char = ']'
) : ComplexTokenizer<E>(
    type,
    listOf(
        SubTokenizer(SingleCharTokenizer(leftBracketType, leftBracket), 1, 1),
        subTokenizer,
        SubTokenizer(SingleCharTokenizer(rightBracketType, rightBracket), 1, 1)
    ))