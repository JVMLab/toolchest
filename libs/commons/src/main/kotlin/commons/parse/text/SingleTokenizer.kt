package com.jvmlab.commons.parse.text



/**
 * Represents a [AbstractTokenizer] which produces only a single type of tokens
 *
 * @property type is a type of token to be produced
 */
abstract class SingleTokenizer<E: Enum<E>> (val type: E) : AbstractTokenizer<E>()