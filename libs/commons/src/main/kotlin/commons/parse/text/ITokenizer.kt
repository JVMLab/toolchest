package com.jvmlab.commons.parse.text


/**
 * The main interface of the parse.text package. It is implemented by any tokenizer
 * and used in text parsers.
 *
 * An implementation must have the following behaviour:
 * - char processing functions should return [StatusNone] if [ITokenizer] still has initial state
 * after processing a [Char]
 * - char processing functions should return [StatusBuilding] if a token is in progress. In this case
 * [ITokenizer] should keep an internal state to be ready to correctly process a next [Char] from a [CharSequence]
 * - char processing functions should return a [StatusFinished] upon successful completion of the tokenization
 */
interface ITokenizer<E: Enum<E>> : IStartTokenizer<E>, IProcessTokenizer<E> {
  override fun reset() = StatusNone(this)
}