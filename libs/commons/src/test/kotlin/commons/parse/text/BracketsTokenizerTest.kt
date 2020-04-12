package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BracketsTokenizerTest {

  @Test
  fun `brackets with single char`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        SubTokenizer(SingleCharTokenizer(TokenType.DEFAULT,'x')),
        TokenType.L_BR, TokenType.R_BR
    )

    tokenizer.processChar('[', 0, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "left bracket")

    tokenizer.processChar('x', 1, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "inner char x")

    tokenizer.processChar(']', 2, true)
    assertEquals(StatusFinished, tokenizer.getBuildingStatus(), "right bracket")

    ComplexToken(
        TokenType.BRACKETS, 0, 2,
        listOf<Token<TokenType>>(
            Token(TokenType.L_BR,0,0),
            Token(TokenType.DEFAULT,1,1),
            Token(TokenType.R_BR,2,2)
        )
    ).assertComplexEquals(tokenizer.buildToken())
  }

}