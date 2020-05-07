package com.jvmlab.commons.parse.text


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
    tokenizer.startProcessing('a', 0)
        .assertBuilding().processChar('_')
        .assertFinish().createToken()
        .assertEquals(expectedWord0)
  }

  @Test
  fun `matching whitespace not last`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processChar('_')
        .assertFinish().createToken()
        .assertEquals(expectedWhitespace0)
  }

  @Test
  fun `matching both not last`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processChar('a')
        .assertFinish().createToken()
        .assertEquals(expectedWhitespace0)
  }

  @Test
  fun `matching word last`() {
    tokenizer.startProcessing('a', 0)
        .assertBuilding().processLastChar('b')
        .assertFinish().createToken()
        .assertEquals(expectedWord1)
  }

  @Test
  fun `matching whitespace last`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processLastChar(' ')
        .assertFinish().createToken()
        .assertEquals(expectedWhitespace1)
  }

  @Test
  fun `matching both last`() {
    tokenizer.startProcessing('a', 0)
        .assertBuilding().processLastChar(' ')
        .assertFinish().createToken()
        .assertEquals(expectedWord0)
  }
}