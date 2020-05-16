package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.BeforeEach


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BracketsTokenizerTest {

  private  val bracketsSingleChar = BracketsTokenizer(
      TokenType.BRACKETS,
      SingleCharTokenizer(TokenType.DEFAULT,'x'),
      TokenType.L_BR, TokenType.R_BR
  )

  @BeforeEach
  fun reset() {
    bracketsSingleChar.reset()
  }


  @Test
  fun `brackets with single char`() {
    val source = "[x]"
    var idx = 0

    bracketsSingleChar.startProcessing(source[idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processLastChar(source[++idx])
        .assertFinish(source).createToken()
        .assertEquals(
            ComplexToken(
                TokenType.BRACKETS, 0, 2,
                listOf(
                    Token(TokenType.L_BR,0,0),
                    Token(TokenType.DEFAULT,1,1),
                    Token(TokenType.R_BR,2,2)
                )
            )
        )
  }


  @Test
  fun `empty brackets with SingleCharTokenizer`() {
    val source = "[]"
    var idx = 0

    bracketsSingleChar.startProcessing(source[idx])
        .assertBuilding(0, idx, source).processLastChar(source[++idx])
        .assertFinish(source).createToken()
        .assertEquals(
            ComplexToken(
                TokenType.BRACKETS, 0, 1,
                listOf(
                    Token(TokenType.L_BR, 0, 0),
                    Token(TokenType.R_BR, 1, 1)
                )
            )
        )
  }


  @Test
  fun `brackets with 2 chars`() {
    val source = "[xx]"
    var idx = 0

    bracketsSingleChar.startProcessing(source[idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processLastChar(source[++idx])
        .assertFinish(source).createToken()
        .assertEquals(
            ComplexToken(
                TokenType.BRACKETS, 0, 3,
                listOf(
                    Token(TokenType.L_BR, 0, 0),
                    Token(TokenType.DEFAULT, 1, 1),
                    Token(TokenType.DEFAULT, 2, 2),
                    Token(TokenType.R_BR, 3, 3)
                )
            )
        )
  }


  @Test
  fun `negative - brackets with single char`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        // exactly 1 SingleChar token is expected
        SubTokenizer(SingleCharTokenizer(TokenType.DEFAULT,'x'), 1, 1),
        TokenType.L_BR, TokenType.R_BR
    )

    val source = "[xx"
    var idx = 0

    tokenizer.startProcessing(source[idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertFailed(0, idx)
  }


  @Test
  fun `brackets with word`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        WordTokenizer(TokenType.WORD),
        TokenType.L_BR, TokenType.R_BR
    )

    val source = "[12]"
    var idx = 0

    tokenizer.startProcessing(source[idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processLastChar(source[++idx])
        .assertFinish(source).createToken()
        .assertEquals(
            ComplexToken(
                TokenType.BRACKETS, 0, 3,
                listOf(
                    Token(TokenType.L_BR, 0, 0),
                    Token(TokenType.WORD, 1, 2),
                    Token(TokenType.R_BR, 3, 3)
                )
            )
        )
  }


  @Test
  fun `brackets with comma-separated words`() {
    val tokenizer = BracketsTokenizer(
        TokenType.BRACKETS,
        AlternativeTokenizer(
            TokenType.DEFAULT,
            listOf(
                WordTokenizer(TokenType.WORD),
                SingleCharTokenizer(TokenType.COMMA,',')
            )
        ),
        TokenType.L_BR, TokenType.R_BR
    )

    val source = "[x,y]"
    var idx = 0

    tokenizer.startProcessing(source[idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processChar(source[++idx])
        .assertBuilding(0, idx, source).processLastChar(source[++idx])
        .assertFinish(source).createToken()
        .assertEquals(
            ComplexToken(
                TokenType.BRACKETS, 0, 4,
                listOf(
                    Token(TokenType.L_BR, 0, 0),
                    Token(TokenType.WORD, 1, 1),
                    Token(TokenType.COMMA, 2, 2),
                    Token(TokenType.WORD, 3, 3),
                    Token(TokenType.R_BR, 4, 4)
                )
            )
        )
  }
}
