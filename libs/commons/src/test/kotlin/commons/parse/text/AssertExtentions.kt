package com.jvmlab.commons.parse.text

import com.jvmlab.commons.parse.Parsed
import com.jvmlab.commons.parse.ParsedKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll


fun Token<TokenType>.assertEquals(token: Token<TokenType>, message: String = "token") {
  assertAll(message,
      { assertEquals(type, token.type, "type mismatch") },
      { assertEquals(start, token.start, "start mismatch") },
      { assertEquals(finish, token.finish, "finish mismatch") }
  )
}


fun ComplexToken<TokenType>.assertComplexEquals(token: ComplexToken<TokenType>, message: String = "token") {
  this.assertEquals(token, message)
  assertEquals(subTokens.size, token.subTokens.size, "sub-tokens size mismatch")

  subTokens.forEachIndexed { idx, t ->
    val expectedSubToken = token.subTokens[idx]
    if ((t is ComplexToken) && (expectedSubToken is ComplexToken)) {
      t.assertComplexEquals(expectedSubToken, "sub-token $idx")
    } else {
      t.assertEquals(expectedSubToken, "sub-token $idx")
    }
  }
}


/**
 * Verifies actual [Parsed] object and compares its list of tokens with the [tokenList]
 *
 * @param tokenList is the expected list of [Token]s
 */
fun Parsed<CharSequence, List<Token<TokenType>>>.assertEquals(
    tokenList: List<Token<TokenType>>,
    message: String = "token list") {
  assertEquals(1, result.size, "$message: result size")
  assertEquals(ParsedKey.PARSED_STRING.key, result.keys.first(), "$message: result key")

  result[ParsedKey.PARSED_STRING.key]?.forEachIndexed { idx, t ->
    val expectedToken = tokenList[idx]
    if ((t is ComplexToken) && (expectedToken is ComplexToken)) {
      t.assertComplexEquals(expectedToken, "token $idx")
    } else {
      t.assertEquals(expectedToken, "token $idx")
    }
  }
}