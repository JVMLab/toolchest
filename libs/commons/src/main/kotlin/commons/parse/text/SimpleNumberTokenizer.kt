package com.jvmlab.commons.parse.text

/**
 * A [GenericTokenizer] to create word tokens with [Char]s satisfying [Char.isDigit]
 * condition (unsigned whole numbers with possible leading zeros)
 */
class SimpleNumberTokenizer<E: Enum<E>>(type: E) : GenericTokenizer<E>(type, Char::isDigit)