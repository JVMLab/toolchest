package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SimpleNumberTokenizerTest {
  private val tokenizer = SimpleNumberTokenizer(TokenType.NUMBER)
  private val expectedToken0 = Token(TokenType.NUMBER, 0, 0)
  private val expectedToken1 = Token(TokenType.NUMBER, 0, 1)
  private val expectedToken2 = Token(TokenType.NUMBER, 0, 2)

  @BeforeEach
  fun reset() {
    tokenizer.reset()
  }

  @Test
  fun `non matching not last`() {
    tokenizer.startProcessing('x', 0).assertNone()
  }

  @Test
  fun `non matching last`() {
    tokenizer.startProcessingLast('x', 0).assertNone()
  }

  @Test
  fun `matching not last`() {
    tokenizer.startProcessing('1', 0)
        .assertBuilding().processChar(' ')
        .assertFinish().createToken()
        .assertEquals(expectedToken0)
  }

  @Test
  fun `matching last`() {
    tokenizer.startProcessing('1', 0)
        .assertBuilding().processLastChar('2')
        .assertFinish().createToken()
        .assertEquals(expectedToken1)
  }

  @Test
  fun `matching last space`() {
    tokenizer.startProcessing('1', 0)
        .assertBuilding().processLastChar(' ')
        .assertFinish().createToken()
        .assertEquals(expectedToken0)
  }
  @Test
  fun `matching long last`() {
    tokenizer.startProcessing('1', 0)
        .assertBuilding().processChar('2')
        .assertBuilding().processLastChar('3')
        .assertFinish().createToken()
        .assertEquals(expectedToken2)
  }

  @Test
  fun `matching long last space`() {
    tokenizer.startProcessing('1', 0)
        .assertBuilding().processChar('2')
        .assertBuilding().processChar('3')
        .assertBuilding().processLastChar(' ')
        .assertFinish().createToken()
        .assertEquals(expectedToken2)
  }
}
