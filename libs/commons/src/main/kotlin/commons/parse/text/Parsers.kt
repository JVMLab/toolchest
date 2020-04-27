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
/*
fun <E: Enum<E>> CharSequence.parse(
    tokenizer: ITokenizer<E>,
    defaultTokenType: E? = null): Parsed<CharSequence, List<Token<E>>> =
    Parsed(this) { charSequence -> parse(charSequence, tokenizer, defaultTokenType) }


*/
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
 *//*

private fun <E: Enum<E>> parse(
    charSequence: CharSequence,
    tokenizer: ITokenizer<E>,
    defaultTokenType: E?
): Map<String, List<Token<E>>> {
  val tokenList = ArrayList<Token<E>>() // a list of tokens to be returned as a value in the Map
  var token: Token<E> // a token built by the currentBuilder
  var defaultStart: Int = 0 // a start of a default token, used when defaultTokenType is not null
  var idx: Int = 0 // current index in the charSequence
  var char: Char // current char in the charSequence
  var isLast: Boolean // true if char is the last Char in the charSequence

  while (idx < charSequence.length) {
    char = charSequence[idx]
    isLast = (idx == charSequence.length - 1)
    when (val status = tokenizer.getBuildingStatus()) {

      StatusNone -> {
        tokenizer.processChar(char, idx, isLast)
        if (isLast) {
          if (StatusNone == tokenizer.getBuildingStatus()) {
            idx++ // to exit the loop
            // Add a default token if the last char doesn't start a token
            tokenList.addDefaultToken(defaultTokenType, defaultStart, idx)
          }
          // don't increment idx here to process a new BuildingStatus in the next iteration
        } else {
          idx++
        }
      }

      StatusBuilding -> {
        // changes this.status and may increment this.finish if still in building
        tokenizer.processChar(char, idx, isLast)
        if (isLast) {
          // don't increment idx here to process a new status in the next iteration
          if (StatusBuilding == tokenizer.getBuildingStatus())
            throw IllegalStateException(
                "Unexpected state: $StatusBuilding at the last position $idx")
        } else {
          // increments idx if still in building or
          // keeps it when this.finish was not incremented in tokenizer.processChar()
          idx = tokenizer.getCurrentFinish() + 1
        }
      }

      StatusFinished -> {
        token = tokenizer.buildToken()
        tokenList.addDefaultToken(defaultTokenType, defaultStart, token.start)
        tokenList.add(token)
        idx = token.finish + 1 // sets idx to a next char after the current token
        defaultStart = idx
      }

      StatusCancelled -> {
        if (isLast) // Add a default token till the end of charSequence in case of the last char
          tokenList.addDefaultToken(defaultTokenType, defaultStart, idx)
        idx = tokenizer.getCurrentFinish() + 1
      }

      is StatusFailed -> {
        throw IllegalStateException("Parsing error at position $idx with reason: ${status.reason}")
      }
    }
  }

  return mapOf(ParsedKey.PARSED_STRING.key to tokenList)
}


*/
/**
 * Adds a default token if required
 *//*

private fun <E: Enum<E>> MutableList<Token<E>>.addDefaultToken(
    defaultTokenType: E?,
    defaultStart: Int,
    nextTokenStart: Int) {
  defaultTokenType?.let {
    if (defaultStart < nextTokenStart)
      this.add(Token<E>(it, defaultStart, nextTokenStart - 1))
  }
}*/
