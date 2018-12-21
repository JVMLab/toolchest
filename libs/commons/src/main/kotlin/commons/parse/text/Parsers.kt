package com.jvmlab.commons.parse.text

import com.jvmlab.commons.parse.ParsedKey


/*
 * Contains extension functions for text parsers
 */



/**
 * Private function which does all heavy-lifting to parse a [CharSequence], all public functions
 * are just wrappers around it
 *
 * @param tokenizer an [AbstractTokenizer] used to split the [CharSequence] into tokens
 * @param defaultTokenType is a type of a default token, which is created whenever the [tokenizer]
 * could not identify a token, mostly for whitespaces (but not only). If null then default
 * tokens will not be created
 */
private fun <E: Enum<E>> CharSequence.parseCharSequence(
    tokenizer: AbstractTokenizer<E>,
    defaultTokenType: E?
): List<Token<E>> {
  val tokenList = ArrayList<Token<E>>()
  var currentBuilder: TokenBuilder<E>? = null
  var defaultBuilder: TokenBuilder<E>? = null

  this.forEachIndexed { idx: Int, char: Char ->
    currentBuilder?.apply {

    } ?: run {
      currentBuilder = tokenizer.firstChar(char, idx)

    }
  }

  return tokenList
}