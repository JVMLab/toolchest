package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.BeforeEach


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SingeCharTokenizerTest {
  private val tokenizer = SingleCharTokenizer<TokenType>(TokenType.DEFAULT, '$')
  private val expectedToken = Token(TokenType.DEFAULT, 0, 0)

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
    tokenizer.processChar('$', 0, false)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished, "status " + tokenizer.getBuildingStatus())

    val token = tokenizer.buildToken()
    expectedToken.assertEquals(token)
  }

  @Test
  fun `matching last`() {
    tokenizer.processChar('$', 0, true)
    assertEquals(tokenizer.getBuildingStatus(),StatusFinished)

    val token = tokenizer.buildToken()
    expectedToken.assertEquals(token)
  }
}
