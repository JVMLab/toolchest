package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BracketsTokenizerTest {

  @Test
  fun `brackets with single char`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        SingleCharTokenizer(TokenType.DEFAULT,'x'),
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
        listOf(
            Token(TokenType.L_BR,0,0),
            Token(TokenType.DEFAULT,1,1),
            Token(TokenType.R_BR,2,2)
        )
    ).assertComplexEquals(tokenizer.buildToken())
  }


  @Test
  fun `empty brackets with SingleCharTokenizer`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        SingleCharTokenizer(TokenType.DEFAULT,'x'),
        TokenType.L_BR, TokenType.R_BR
    )

    tokenizer.processChar('[', 0, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "left bracket")

    tokenizer.processChar(']', 1, true)
    assertEquals(StatusFinished, tokenizer.getBuildingStatus(), "right bracket")

    ComplexToken(
        TokenType.BRACKETS, 0, 1,
        listOf(
            Token(TokenType.L_BR,0,0),
            Token(TokenType.R_BR,1,1)
        )
    ).assertComplexEquals(tokenizer.buildToken())
  }


  @Test
  fun `brackets with 2 chars`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        SingleCharTokenizer(TokenType.DEFAULT,'x'),
        TokenType.L_BR, TokenType.R_BR
    )

    tokenizer.processChar('[', 0, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "left bracket")

    tokenizer.processChar('x', 1, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "first inner char x")

    tokenizer.processChar('x', 2, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "second inner char x")

    tokenizer.processChar(']', 3, true)
    assertEquals(StatusFinished, tokenizer.getBuildingStatus(), "right bracket")

    ComplexToken(
        TokenType.BRACKETS, 0, 3,
        listOf(
            Token(TokenType.L_BR,0,0),
            Token(TokenType.DEFAULT,1,1),
            Token(TokenType.DEFAULT,2,2),
            Token(TokenType.R_BR,3,3)
        )
    ).assertComplexEquals(tokenizer.buildToken())
  }


  @Test
  fun `negative - brackets with single char`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        SubTokenizer(SingleCharTokenizer(TokenType.DEFAULT,'x'), 1, 1), // exactly 1 SingleCharTokenizer is expected
        TokenType.L_BR, TokenType.R_BR
    )

    tokenizer.processChar('[', 0, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "left bracket")

    tokenizer.processChar('x', 1, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "first inner char x")

    tokenizer.processChar('x', 2, false)
    assertTrue(tokenizer.getBuildingStatus() is StatusFailed, "second inner char x instead of closing bracket")
  }

  @Test
  fun `brackets with word`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        WordTokenizer(TokenType.WORD),
        TokenType.L_BR, TokenType.R_BR
    )

    tokenizer.processChar('[', 0, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "left bracket")

    tokenizer.processChar('1', 1, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "inner char 1")

    tokenizer.processChar('2', 2, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "inner char 2")

    tokenizer.processChar(']', 3, true)
    assertEquals(StatusFinished, tokenizer.getBuildingStatus(), "right bracket")

    ComplexToken(
        TokenType.BRACKETS, 0, 3,
        listOf(
            Token(TokenType.L_BR,0,0),
            Token(TokenType.WORD,1,2),
            Token(TokenType.R_BR,3,3)
        )
    ).assertComplexEquals(tokenizer.buildToken())
  }


  @Test
  fun `brackets with comma-separated words`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        MultiTokenizer(
            TokenType.DEFAULT,
            listOf<ITokenizer<TokenType>>(
                WordTokenizer(TokenType.WORD),
                SingleCharTokenizer(TokenType.COMMA,',')
            )
        ),
        TokenType.L_BR, TokenType.R_BR
    )

    tokenizer.processChar('[', 0, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "left bracket")

    tokenizer.processChar('x', 1, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "inner char x")

    tokenizer.processChar(',', 2, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "inner comma")

    tokenizer.processChar('y', 3, false)
    assertEquals(StatusBuilding, tokenizer.getBuildingStatus(), "inner char y")

    tokenizer.processChar(']', 4, true)
    assertEquals(StatusFinished, tokenizer.getBuildingStatus(), "right bracket")

    ComplexToken(
        TokenType.BRACKETS, 0, 4,
        listOf(
            Token(TokenType.L_BR,0,0),
            Token(TokenType.WORD,1,1),
            Token(TokenType.COMMA,2,2),
            Token(TokenType.WORD,3,3),
            Token(TokenType.R_BR,4,4)
        )
    ).assertComplexEquals(tokenizer.buildToken())
  }
}