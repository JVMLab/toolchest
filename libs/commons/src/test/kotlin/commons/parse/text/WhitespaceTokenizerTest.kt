package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class WhitespaceTokenizerTest {
  private val tokenizer = WhitespaceTokenizer(TokenType.WHITESPACE)
  private val expectedToken0 = Token(TokenType.WHITESPACE, 0, 0)
  private val expectedToken1 = Token(TokenType.WHITESPACE, 0, 1)
  private val expectedToken2 = Token(TokenType.WHITESPACE, 0, 2)

  @BeforeEach
  fun reset() {
    tokenizer.reset()
  }

  @Test
  fun `non matching not last`() {
    tokenizer.startProcessing('X', 0)
        .assertCancelled().reset()
        .assertNone()
  }

  @Test
  fun `non matching last`() {
    tokenizer.startProcessingLast('X', 0)
        .assertCancelled().reset()
        .assertNone()
  }

  @Test
  fun `matching not last`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processChar('X')
        .assertFinish().createToken()
        .assertEquals(expectedToken0)
  }

  @Test
  fun `matching last`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processLastChar(' ')
        .assertFinish().createToken()
        .assertEquals(expectedToken1)
  }

  @Test
  fun `matching last non-space`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processLastChar('X')
        .assertFinish().createToken()
        .assertEquals(expectedToken0)
  }

  @Test
  fun `matching long last`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processChar(' ')
        .assertBuilding().processLastChar(' ')
        .assertFinish().createToken()
        .assertEquals(expectedToken2)
  }

  @Test
  fun `matching long last non-space`() {
    tokenizer.startProcessing(' ', 0)
        .assertBuilding().processChar(' ')
        .assertBuilding().processChar(' ')
        .assertBuilding().processLastChar('X')
        .assertFinish().createToken()
        .assertEquals(expectedToken2)
  }
}
