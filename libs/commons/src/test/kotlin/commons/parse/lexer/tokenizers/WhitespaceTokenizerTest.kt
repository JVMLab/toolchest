package com.jvmlab.commons.parse.lexer.tokenizers

import com.jvmlab.commons.parse.lexer.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
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
    val source = " X"

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processChar(source[1])
        .assertFinish(expectedToken0.sequence(source)).createToken()
        .assertEquals(expectedToken0)
  }


  @Test
  fun `matching last`() {
    val source = "  "

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processLastChar(source[1])
        .assertFinish(expectedToken1.sequence(source)).createToken()
        .assertEquals(expectedToken1)
  }


  @Test
  fun `matching last non-space`() {
    val source = " X"

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processLastChar(source[1])
        .assertFinish(expectedToken0.sequence(source)).createToken()
        .assertEquals(expectedToken0)
  }


  @Test
  fun `matching long last`() {
    val source = "   "

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processChar(source[1])
        .assertBuilding(0, 1, source).processLastChar(source[2])
        .assertFinish(expectedToken2.sequence(source)).createToken()
        .assertEquals(expectedToken2)
  }


  @Test
  fun `matching long last non-space`() {
    val source = "   X"

    tokenizer.startProcessing(source[0])
        .assertBuilding(0, 0, source).processChar(source[1])
        .assertBuilding(0, 1, source).processChar(source[2])
        .assertBuilding(0, 2, source).processLastChar(source[3])
        .assertFinish(expectedToken2.sequence(source)).createToken()
        .assertEquals(expectedToken2)
  }
}
