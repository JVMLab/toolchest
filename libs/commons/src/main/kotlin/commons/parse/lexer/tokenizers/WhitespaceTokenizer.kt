package com.jvmlab.commons.parse.lexer.tokenizers


/**
 * A [GenericTokenizer] to create word tokens with [Char]s satisfying [Char.isWhitespace]
 * condition
 */

class WhitespaceTokenizer<E: Enum<E>>(type: E) : GenericTokenizer<E>(type, Char::isWhitespace)
