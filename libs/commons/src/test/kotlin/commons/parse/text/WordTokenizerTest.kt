package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class WordTokenizerTest {
  private val tokenizer = WordTokenizer(TokenType.WORD)
  private val expectedToken0 = Token(TokenType.WORD, 0, 0)
  private val expectedToken1 = Token(TokenType.WORD, 0, 1)

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
    tokenizer.processChar('a', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusBuilding)

    tokenizer.processChar(' ', 1, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished)

    tokenizer.buildToken().assertEquals(expectedToken0)
  }

  @Test
  fun `matching last`() {
    tokenizer.processChar('a', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusBuilding)

    tokenizer.processChar('b', 1, true)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished)

    tokenizer.buildToken().assertEquals(expectedToken1)
  }

  @Test
  fun `matching last space`() {
    tokenizer.processChar('a', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusBuilding)

    tokenizer.processChar(' ', 1, true)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished)

    tokenizer.buildToken().assertEquals(expectedToken0)
  }

}