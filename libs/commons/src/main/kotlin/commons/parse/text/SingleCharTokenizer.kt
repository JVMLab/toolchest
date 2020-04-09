package com.jvmlab.commons.parse.text


/**
 * A [GenericTokenizer] which creates a single-[Char] tokens. Can be used to split a [CharSequence]
 * by a single-[Char] separators
 *
 * @property tokenChar represents a [Char] used in created tokens
 */
class SingleCharTokenizer<E: Enum<E>>(
    type: E,
    private val tokenChar: Char) :
    GenericTokenizer<E>(
        type = type,
        checkFirstChar = { char: Char -> (char == tokenChar) },
        firstCharStatus = StatusFinished,
        checkNextChar = { false }
    )
