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
  var token: Token<E> // a token built by the currentBuilder
  var defaultStart = 0 // a start of a default token, used when defaultTokenType is not null
  var idx = 0 // current index in the charSequence
  var char: Char // current char in the charSequence
  var status: TokenizerStatus = StatusNone<E>(tokenizer)


  while (idx < charSequence.length - 1) {
    char = charSequence[idx]
    when (status) {

      is StatusNone<*> -> {
        status = status.startProcessing(char, idx)
        when (status) {
          is StatusNone<*>,
          is StatusBuilding<*> -> idx++
          is FinalModifiedStatus -> { /* just skip, will be processed in the next iteration */ }
        }
      }

      is StatusBuilding<*> -> {
        // increments idx if still in building or
        // keeps it when this.finish was not incremented in tokenizer.processChar()
        status = status.processChar(char)
        when (status) {
          is StatusBuilding<*> -> idx = status.finish + 1
          is FinalModifiedStatus -> { /* just skip, will be processed in the next iteration */ }
          is StatusNone<*> -> throw IllegalStateException("Unexpected $status at position $idx")
        }
      }

      is StatusFinished<*> -> {
        @Suppress("UNCHECKED_CAST")
        token = status.createToken() as Token<E>
        status = status.reset()
        tokenList.addDefaultToken(defaultTokenType, defaultStart, token.start)
        tokenList.add(token)
        idx = token.finish + 1 // sets idx to a next char after the current token
        defaultStart = idx
      }

      is StatusCancelled<*> -> {
        idx = status.finish + 1
        status = status.reset()
      }

      is StatusFailed<*> -> throw IllegalStateException("Parsing error at position $idx with reason: ${status.reason}")
    }
  }

  //idx == charSequence.length - 1
  char = charSequence[idx]  // it's not yet parsed

  status = when (status) {
    is StatusNone<*> -> status.startProcessingLast(char, idx)
    is StatusBuilding<*> -> status.processLastChar(char)
    is FinalModifiedStatus -> throw IllegalStateException("Unexpected $status before position $idx")
  }

  when (status) {
    is StatusNone<*>,
    is StatusCancelled<*> -> tokenList.addDefaultToken(defaultTokenType, defaultStart, idx + 1)
    is StatusFinished<*> -> {
      @Suppress("UNCHECKED_CAST")
      token = status.createToken() as Token<E>
      tokenList.addDefaultToken(defaultTokenType, defaultStart, token.start)
      tokenList.add(token)
    }
    is StatusBuilding<*> -> throw IllegalStateException("Unexpected $status at position $idx")
    is StatusFailed<*> -> throw IllegalStateException("Parsing error at position $idx with reason: ${status.reason}")
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
