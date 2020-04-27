package com.jvmlab.commons.parse.text


/**
 * A [GenericSingleCharTokenizer] which creates single-char tokens. Can be used to split a [CharSequence]
 * by single-char separators e.g. commas
 *
 * @property tokenChar represents a [Char] used in created tokens
 */
class SingleCharTokenizer<E: Enum<E>>(type: E, private val tokenChar: Char) :
    GenericSingleCharTokenizer<E>(
        type = type,
        checkChar = { char: Char -> (char == tokenChar) }
    )
