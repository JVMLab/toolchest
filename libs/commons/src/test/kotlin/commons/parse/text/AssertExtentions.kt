package com.jvmlab.commons.parse.text


/**
 * Extension assertion functions for package classes under test. A receiver type is supposed to be used as an actual
 * value, and a function argument as an expected value.
 */


import com.jvmlab.commons.parse.Parsed
import com.jvmlab.commons.parse.ParsedKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll


fun Token<TokenType>.assertEquals(expectedToken: Token<TokenType>, message: String = expectedToken.type.toString()) {
  assertAll(message,
      { assertEquals(expectedToken.type, type, "type mismatch") },
      { assertEquals(expectedToken.start, start, "start mismatch") },
      { assertEquals(expectedToken.finish, finish, "finish mismatch") }
  )
}


fun ComplexToken<TokenType>.assertComplexEquals(expectedToken: ComplexToken<TokenType>,
    message: String = expectedToken.type.toString()) {

  this.assertEquals(expectedToken, message)
  assertEquals(subTokens.size, expectedToken.subTokens.size, "sub-tokens size mismatch")

  subTokens.forEachIndexed { idx, t ->
    val expectedSubToken = expectedToken.subTokens[idx]
    val expectedSubTokenType = expectedSubToken.type
    val msg = "sub-token $expectedSubTokenType #$idx"
    if ((t is ComplexToken) && (expectedSubToken is ComplexToken)) {
      t.assertComplexEquals(expectedSubToken, msg)
    } else {
      t.assertEquals(expectedSubToken, msg)
    }
  }
}


/**
 * Verifies actual [Parsed] object and compares its list of tokens with the [expectedTokenList]
 *
 * @param expectedTokenList is the expected list of [Token]s
 */
fun Parsed<CharSequence, List<Token<TokenType>>>.assertEquals(
    expectedTokenList: List<Token<TokenType>>, message: String = "Parsed") {

  assertEquals(1, result.size, "$message: result size")
  assertEquals(ParsedKey.PARSED_STRING.key, result.keys.first(), "$message: result key")

  result[ParsedKey.PARSED_STRING.key]?.forEachIndexed { idx, t ->
    val expectedToken = expectedTokenList[idx]
    val expectedTokenType = expectedToken.type
    val msg = "token $expectedTokenType #$idx"
    if ((t is ComplexToken) && (expectedToken is ComplexToken)) {
      t.assertComplexEquals(expectedToken, msg)
    } else {
      t.assertEquals(expectedToken, msg)
    }
  }

}
