package com.jvmlab.commons.parse.text

import com.jvmlab.commons.parse.Parsed
import com.jvmlab.commons.parse.ParsedKey


/*
 * Contains extension functions implementing text parsers
 */


fun <E: Enum<E>> CharSequence.parse(
    tokenizer: AbstractTokenizer<E>,
    defaultTokenType: E? = null): Parsed<CharSequence> = Parsed(this) { charSequence ->
      parse(charSequence, tokenizer, defaultTokenType)
    }


/**
 * Private function which does all heavy-lifting to parse a [charSequence], all public functions
 * are just wrappers around it.
 *
 * @param charSequence is a [CharSequence] to be parsed
 * @param tokenizer is an [AbstractTokenizer] used to split the [charSequence] into tokens
 * @param defaultTokenType is a type of a default token, which is created whenever the [tokenizer]
 * could not identify a token, usually for whitespaces (but not only). If null then default
 * tokens will not be created
 *
 * @return a single key [Map] with list of tokens produced by [tokenizer] along with default tokens
 * (in case of [defaultTokenType] is not null) as a value and [ParsedKey.PARSED_STRING.key] key
 */
private fun <E: Enum<E>> parse(
    charSequence: CharSequence,
    tokenizer: AbstractTokenizer<E>,
    defaultTokenType: E?
): Map<String, List<Token<E>>> {
  val tokenList = ArrayList<Token<E>>() // a list of tokens to be returned
  var currentBuilder: TokenBuilder<E>? = null // a current token builder provided be tokenizer
  var token: Token<E> // a token built by the currentBuilder
  var defaultStart: Int = 0 // a start of a default token, used when defaultTokenType is not null
  var idx: Int = 0 // current index in the CharSequence
  var char: Char // current char in the CharSequence

  while (idx < charSequence.length) {
    char = charSequence[idx]
    currentBuilder?.apply {
      when (this.status) {

        BuildingStatus.BUILDING -> {
          tokenizer.nextChar(char, this)
          idx = this.finish + 1
        }

        BuildingStatus.FINISHED -> {
          token = this.build()
          currentBuilder = null
          tokenList.addDefaultToken(defaultTokenType, defaultStart, token.start)
          tokenList.add(token)
          idx = token.finish + 1
          defaultStart = idx
        }

        BuildingStatus.CANCELLED -> {
          idx = this.finish + 1
          currentBuilder = null
        }

        BuildingStatus.FAILED -> {
          throw IllegalStateException("Parsing error of ${this.type} at position ${this.finish}")
        }
      }
    } ?: run {
      currentBuilder = tokenizer.firstChar(char, idx)
      idx++
    }
  }

  /*
  Process a last char of the charSequence if we have the currentBuilder in
  the BuildingStatus.BUILDING status, and generate default token if required
  */
  currentBuilder?.apply {
    if (this.status == BuildingStatus.BUILDING) {
      idx-- //sets idx to the last char in charSequence
      char = charSequence[idx]
      tokenizer.lastChar(char, this)
      when (this.status) {
        BuildingStatus.FINISHED -> {
          token = this.build()
          tokenList.addDefaultToken(defaultTokenType, defaultStart, token.start)
          tokenList.add(token)
        }

        BuildingStatus.CANCELLED -> {
          tokenList.addDefaultToken(defaultTokenType, defaultStart, idx)
        }

        BuildingStatus.BUILDING -> {
          throw IllegalStateException("Unexpected state: ${BuildingStatus.BUILDING} " +
                  "of ${this.type} at position ${this.finish}")
        }

        BuildingStatus.FAILED -> {
          throw IllegalStateException("Parsing error of ${this.type} at position ${this.finish}")
        }
      }
    }
  }

  return mapOf(ParsedKey.PARSED_STRING.key to tokenList)
}



private fun <E: Enum<E>> MutableList<Token<E>>.addDefaultToken(
    defaultTokenType: E?,
    defaultStart: Int,
    nextTokenStart: Int
) {
  defaultTokenType?.let { defaultTokenType ->
    if (defaultStart < nextTokenStart) {
      this.add(Token<E>(defaultTokenType, defaultStart, nextTokenStart - 1))
    }
  }
}