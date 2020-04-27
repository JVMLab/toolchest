package com.jvmlab.commons.parse.text

/**
 *
 * @property type the type of a token in progress or a default token type made by the [ITokenizer]
 */
interface ITokenizerType<E: Enum<E>> {
  val type: E
}