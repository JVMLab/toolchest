package com.jvmlab.commons.parse.text

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SingeCharTokenizerTest {
  private val tokenizer = SingleCharTokenizer<TokenType>(TokenType.DEFAULT, '$')


  @Test
  fun `non matching not last`() {
    tokenizer.processChar(' ', 0, false)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.NONE)
  }

  @Test
  fun `non matching last`() {
    tokenizer.processChar(' ', 0, true)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.NONE)
  }

  @Test
  fun `matching not last`() {
    tokenizer.processChar('$', 0, false)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.FINISHED)

    val token = tokenizer.buildToken()
    assertThat(token).isEqualToComparingFieldByField(Token(TokenType.DEFAULT, 0, 0))
  }

  @Test
  fun `matching last`() {
    tokenizer.processChar('$', 0, true)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.FINISHED)

    val token = tokenizer.buildToken()
    assertThat(token).isEqualToComparingFieldByField(Token(TokenType.DEFAULT, 0, 0))
  }
}


enum class TokenType {
  DEFAULT
}