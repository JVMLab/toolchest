package com.jvmlab.commons.parse.text

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WordTokenizerTest {
  private val tokenizer = WordTokenizer(TokenType.WORD)


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
    tokenizer.processChar('a', 0, false)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.BUILDING)

    tokenizer.processChar(' ', 1, false)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.FINISHED)

    val token = tokenizer.buildToken()
    assertThat(token).isEqualToComparingFieldByField(Token(TokenType.WORD, 0, 0))
  }

  @Test
  fun `matching last`() {
    tokenizer.processChar('a', 0, false)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.BUILDING)

    tokenizer.processChar('b', 1, true)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.FINISHED)

    val token = tokenizer.buildToken()
    assertThat(token).isEqualToComparingFieldByField(Token(TokenType.WORD, 0, 1))
  }

  @Test
  fun `matching last space`() {
    tokenizer.processChar('a', 0, false)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.BUILDING)

    tokenizer.processChar(' ', 1, true)
    assertThat(tokenizer.getBuildingStatus()).isEqualTo(BuildingStatus.FINISHED)

    val token = tokenizer.buildToken()
    assertThat(token).isEqualToComparingFieldByField(Token(TokenType.WORD, 0, 0))
  }

}