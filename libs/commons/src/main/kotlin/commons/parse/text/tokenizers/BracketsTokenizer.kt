package com.jvmlab.commons.parse.text.tokenizers


private enum class Defaults(val c: Char) {
    LEFT_BRACKET('['),
    RIGHT_BRACKET(']')
}


/**
 * Creates [ComplexToken] for brackets and their internal content as sub-tokens
 *
 */
class BracketsTokenizer<E: Enum<E>> : ComplexTokenizer<E> {

/**
 * the default constructor
 *
 * @param leftBracket represents a left bracket [Char]
 * @param rightBracket represents a right bracket [Char]
 */

    constructor(
        type: E,
        subTokenizer: SubTokenizer<E>,
        leftBracketType: E,
        rightBracketType: E,
        leftBracket: Char = Defaults.LEFT_BRACKET.c,
        rightBracket: Char = Defaults.RIGHT_BRACKET.c
    ) : super (
        type,
        listOf(
            SubTokenizer(SingleCharTokenizer(leftBracketType, leftBracket), 1, 1),
            subTokenizer,
            SubTokenizer(SingleCharTokenizer(rightBracketType, rightBracket), 1, 1)
        )
    )

/**
 * a secondary constructor with unlimited number of [tokenizer]s instead of a [SubTokenizer]
 */

    constructor(type: E,
                tokenizer: IStartTokenizer<E>,
                leftBracketType: E,
                rightBracketType: E,
                leftBracket: Char = Defaults.LEFT_BRACKET.c,
                rightBracket: Char = Defaults.RIGHT_BRACKET.c) : super(
        type,
        listOf(
            SubTokenizer(SingleCharTokenizer(leftBracketType, leftBracket), 1, 1),
            SubTokenizer(tokenizer, 0, 0),
            SubTokenizer(SingleCharTokenizer(rightBracketType, rightBracket), 1, 1)
        )
    )
}
