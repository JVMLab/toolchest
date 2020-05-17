package com.jvmlab.commons.parse.text.tokenizers


import com.jvmlab.commons.parse.text.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.BeforeEach


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AlternativeTokenizerTest {

  private val tokenizer = AlternativeTokenizer(
      TokenType.DEFAULT,
      listOf(
          WordTokenizer(TokenType.WORD),
          WhitespaceTokenizer(TokenType.WHITESPACE)
      )
  )

  private val expectedWord0 = Token(TokenType.WORD, 0, 0)
  private val expectedWord1 = Token(TokenType.WORD, 0, 1)
  private val expectedWhitespace0 = Token(TokenType.WHITESPACE, 0, 0)
  private val expectedWhitespace1 = Token(TokenType.WHITESPACE, 0, 1)

  @BeforeEach
  fun reset() {
    tokenizer.reset()
  }

  @Test
  fun `non matching not last`() {
    tokenizer.startProcessing('_', 0)
        .assertCancelled().reset()
        .assertNone()
  }

  @Test
  fun `non matching last`() {
    tokenizer.startProcessingLast('_', 0)
        .assertCancelled().reset()
        .assertNone()
  }


  @Test
  fun `matching word not last`() {
    val source = "a_"

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processChar(source[1])
        .assertFinish(expectedWord0.sequence(source)).createToken()
        .assertEquals(expectedWord0)
  }


  @Test
  fun `matching whitespace not last`() {
    val source = " _"

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processChar(source[1])
        .assertFinish(expectedWhitespace0.sequence(source)).createToken()
        .assertEquals(expectedWhitespace0)
  }


  @Test
  fun `matching both not last`() {
    val source = " a"

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processChar(source[1])
        .assertFinish(expectedWhitespace0.sequence(source)).createToken()
        .assertEquals(expectedWhitespace0)
  }


  @Test
  fun `matching word last`() {
    val source = "ab"

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processLastChar(source[1])
        .assertFinish(expectedWord1.sequence(source)).createToken()
        .assertEquals(expectedWord1)
  }


  @Test
  fun `matching whitespace last`() {
    val source = "  "

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processLastChar(source[1])
        .assertFinish(expectedWhitespace1.sequence(source)).createToken()
        .assertEquals(expectedWhitespace1)
  }


  @Test
  fun `matching both last`() {
    val source = "a "

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processLastChar(source[1])
        .assertFinish(expectedWord0.sequence(source)).createToken()
        .assertEquals(expectedWord0)
  }
}
