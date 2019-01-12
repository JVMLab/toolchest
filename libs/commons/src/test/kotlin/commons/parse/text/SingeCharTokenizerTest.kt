package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SingeCharTokenizerTest {
  private val tokenizer = SingleCharTokenizer<TokenType>(TokenType.DEFAULT, '$')


  @Test
  fun `non matching not last`() {
    tokenizer.processChar(' ', 0, false)
    assertEquals(BuildingStatus.NONE, tokenizer.getBuildingStatus())
  }

  @Test
  fun `non matching last`() {
    tokenizer.processChar(' ', 0, true)
    assertEquals(BuildingStatus.NONE, tokenizer.getBuildingStatus())
  }

  @Test
  fun `matching not last`() {
    tokenizer.processChar('$', 0, false)
    assertEquals(BuildingStatus.FINISHED, tokenizer.getBuildingStatus())

    val token = tokenizer.buildToken()
    assertEquals(Token(TokenType.DEFAULT, 0, 0), token)
  }

  @Test
  fun `matching last`() {
    tokenizer.processChar('$', 0, true)
    assertEquals(BuildingStatus.FINISHED, tokenizer.getBuildingStatus())

    val token = tokenizer.buildToken()
    assertEquals(Token(TokenType.DEFAULT, 0, 0), token)
  }
}


enum class TokenType {
  DEFAULT
}