package com.jvmlab.commons.parse.text


/**
 * Adds a possibility to modify [type] of an [ITokenizer], to be used in complex tokenizers
 * where token type may change during tokenization
 */
internal interface IModifiableTokenizer<E: Enum<E>> : ITokenizer<E>{
  override var type: E
}