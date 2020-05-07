package com.jvmlab.commons.parse.text

import com.jvmlab.commons.parse.Parsed
import com.jvmlab.commons.parse.ParsedKey


/*
 * Contains extension functions implementing text parsers
 */


/**
 * An extension function to parse [CharSequence] into [Parsed] using [tokenizer]
 *
 * @param tokenizer is an [ITokenizer] used to split [this] [CharSequence] into tokens
 * @param defaultTokenType is a type of a default token, which is created whenever the [tokenizer]
 */
fun <E: Enum<E>> CharSequence.parse(
    tokenizer: IStartTokenizer<E>,
    defaultTokenType: E? = null): Parsed<CharSequence, List<Token<E>>> =
    Parsed(this) { charSequence -> parse(charSequence, tokenizer, defaultTokenType) }


/**
 * Private function which does all heavy-lifting to parse a [charSequence], all public functions
 * are just wrappers around it.
 *
 * @param charSequence is a [CharSequence] to be parsed
 * @param tokenizer is an [ITokenizer] used to split the [charSequence] into tokens
 * @param defaultTokenType is a type of a default token, which is created whenever the [tokenizer]
 * could not identify a token, usually for whitespaces (but not only). If null then default
 * tokens will not be created
 *
 * @return a single key [Map] with list of tokens produced by [tokenizer] along with default tokens
 * (in case of [defaultTokenType] is not null) as a value and [ParsedKey.PARSED_STRING.key] key
 *
 */
private fun <E: Enum<E>> parse(
    charSequence: CharSequence,
    tokenizer: IStartTokenizer<E>,
    defaultTokenType: E?
): Map<String, List<Token<E>>> {
  val tokenList = ArrayList<Token<E>>() // a list of tokens to be returned as a value in the Map
  var defaultStart = 0 // a start of a default token, used when defaultTokenType is not null
  var idx = 0 // current index in the charSequence
  var char: Char // current char in the charSequence

  val initialStatus = tokenizer.reset()
  var status: TokenizerStatus = initialStatus

  fun processFinished(statusFinished: StatusFinished<E>) {
    val token = statusFinished.createToken()
    tokenList.addDefaultToken(defaultTokenType, defaultStart, token.start)
    tokenList.add(token)
    idx = token.finish + 1 // sets idx to a next char after the current token
    defaultStart = idx
  }

  fun processFailed(statusFailed: StatusFailed<*>) {
    throw IllegalStateException("Parsing error at position $idx with reason: ${statusFailed.reason}")
  }

  while (idx < charSequence.length - 1) {
    char = charSequence[idx]
    when (status) {
      is StatusNone<*> -> {
        status = status.startProcessing(char, idx)
        if (status is StatusBuilding<*>)
          idx = status.finish + 1
      }
      is StatusBuilding<*> -> {
        status = status.processChar(char)
        if (status is StatusBuilding<*>)
          idx = status.finish + 1
      }
      is StatusFinished<*> -> {
        @Suppress("UNCHECKED_CAST")
        processFinished(status as StatusFinished<E>)
        status = initialStatus
      }
      is StatusCancelled<*> -> {
        idx = status.finish + 1
        status = initialStatus
      }
      is StatusFailed<*> ->
        processFailed(status)
    }
  }

  // Process the last char, idx == charSequence.length - 1
  while (idx < charSequence.length) {
    char = charSequence[idx]
    when (status) {
      is StatusNone<*> ->
        status = status.startProcessingLast(char, idx)
      is StatusBuilding<*> ->
        status = status.processLastChar(char)
      is StatusFinished<*> -> {
        @Suppress("UNCHECKED_CAST")
        processFinished(status as StatusFinished<E>)
        status = initialStatus
      }
      is StatusCancelled<*> -> {
        idx = status.finish + 1
        tokenList.addDefaultToken(defaultTokenType, defaultStart, idx)
      }
      is StatusFailed<*> ->
        processFailed(status)
    }
  }

  return mapOf(ParsedKey.PARSED_STRING.key to tokenList)
}


/**
 * Adds a default token if required
 */
private fun <E: Enum<E>> MutableList<Token<E>>.addDefaultToken(
    defaultTokenType: E?,
    defaultStart: Int,
    nextTokenStart: Int) {
  defaultTokenType?.let {
    if (defaultStart < nextTokenStart)
      this.add(Token<E>(it, defaultStart, nextTokenStart - 1))
  }
}
