package com.jvmlab.commons.parse.lexer.tokenizers


/**
 * A [GenericTokenizer] to create word tokens with [Char]s satisfying [Char.isLetterOrDigit] condition
 */
class WordTokenizer<E: Enum<E>>(type: E) : GenericTokenizer<E>(type, Char::isLetterOrDigit)
