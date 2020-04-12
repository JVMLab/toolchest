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

  @BeforeEach
  fun reset() {
    tokenizer.reset()
  }

  @Test
  fun `non matching not last`() {
    tokenizer.processChar(' ', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusNone)
  }

  @Test
  fun `non matching last`() {
    tokenizer.processChar(' ', 0, true)
    assertEquals(tokenizer.getBuildingStatus(),StatusNone)
  }

  @Test
  fun `matching not last`() {
    tokenizer.processChar('1', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusBuilding)

    tokenizer.processChar(' ', 1, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished)

    val token = tokenizer.buildToken()
    expectedToken0.assertEquals(token)
  }

  @Test
  fun `matching last`() {
    tokenizer.processChar('1', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusBuilding)

    tokenizer.processChar('2', 1, true)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished)

    val token = tokenizer.buildToken()
    expectedToken1.assertEquals(token)
  }

  @Test
  fun `matching last space`() {
    tokenizer.processChar('1', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusBuilding)

    tokenizer.processChar(' ', 1, true)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished)

    val token = tokenizer.buildToken()
    expectedToken0.assertEquals(token)
  }

}