package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.ParameterizedTest

import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ParsersTest {

  private val singleCharTokenizer = SingleCharTokenizer(TokenType.COMMA, ',')

  fun singleCharArgs(): Stream<Arguments> =
      Stream.of(
          Arguments.of(
              "x,",
              listOf(
                  Token(TokenType.DEFAULT, 0, 0),
                  Token(TokenType.COMMA, 1, 1)
              )
          ),
          Arguments.of(
              ",y",
              listOf(
                  Token(TokenType.COMMA, 0, 0),
                  Token(TokenType.DEFAULT, 1, 1)
              )
          ),
          Arguments.of(
              "x,y",
              listOf(
                  Token(TokenType.DEFAULT, 0, 0),
                  Token(TokenType.COMMA, 1, 1),
                  Token(TokenType.DEFAULT, 2, 2)
              )
          ),
          Arguments.of(
              "x,,y",
              listOf(
                  Token(TokenType.DEFAULT, 0, 0),
                  Token(TokenType.COMMA, 1, 1),
                  Token(TokenType.COMMA, 2, 2),
                  Token(TokenType.DEFAULT, 3, 3)
              )
          )
      )

  @ParameterizedTest(name = "SingleCharTokenizer: <{0}>")
  @MethodSource("singleCharArgs")
  fun singleCharTest(src: String, tokens: List<Token<TokenType>>) {
    src.parse(singleCharTokenizer, TokenType.DEFAULT).assertEquals(tokens)
  }


  private val wordTokenizer = WordTokenizer(TokenType.WORD)

  fun wordArgs(): Stream<Arguments> =
      Stream.of(
          Arguments.of(
              "   ",
              listOf(
                  Token(TokenType.WHITESPACE, 0, 2)
              )
          ),
          Arguments.of(
              "xxx",
              listOf(
                  Token(TokenType.WORD, 0, 2)
              )
          ),
          Arguments.of(
              " xxx",
              listOf(
                  Token(TokenType.WHITESPACE, 0, 0),
                  Token(TokenType.WORD, 1, 3)
              )
          ),
          Arguments.of(
              "xxx ",
              listOf(
                  Token(TokenType.WORD, 0, 2),
                  Token(TokenType.WHITESPACE, 3, 3)
              )
          ),
          Arguments.of(
              "xxx  y",
              listOf(
                  Token(TokenType.WORD, 0, 2),
                  Token(TokenType.WHITESPACE, 3, 4),
                  Token(TokenType.WORD, 5, 5)
              )
          ),
          Arguments.of(
              "xxx  y ",
              listOf(
                  Token(TokenType.WORD, 0, 2),
                  Token(TokenType.WHITESPACE, 3, 4),
                  Token(TokenType.WORD, 5, 5),
                  Token(TokenType.WHITESPACE, 6, 6)
              )
          )
      )

  @ParameterizedTest(name = "WordTokenizer: <{0}>")
  @MethodSource("wordArgs")
  fun wordTest(src: String, tokens: List<Token<TokenType>>) {
    src.parse(wordTokenizer, TokenType.WHITESPACE).assertEquals(tokens)
  }


  private val simpleNumberTokenizer = SimpleNumberTokenizer(TokenType.NUMBER)

  fun simpleNumberArgs(): Stream<Arguments> =
      Stream.of(
          Arguments.of(
              "1X",
              listOf(
                  Token(TokenType.NUMBER, 0, 0),
                  Token(TokenType.DEFAULT, 1, 1)
              )
          ),
          Arguments.of(
              "X2",
              listOf(
                  Token(TokenType.DEFAULT, 0, 0),
                  Token(TokenType.NUMBER, 1, 1)
              )
          ),
          Arguments.of(
              "10 20",
              listOf(
                  Token(TokenType.NUMBER, 0, 1),
                  Token(TokenType.DEFAULT, 2, 2),
                  Token(TokenType.NUMBER, 3, 4)
              )
          ),
          Arguments.of(
              "01XX02",
              listOf(
                  Token(TokenType.NUMBER, 0, 1),
                  Token(TokenType.DEFAULT, 2, 3),
                  Token(TokenType.NUMBER, 4, 5)
              )
          )
      )

  @ParameterizedTest(name = "SimpleNumberTokenizer: \"{0}\"")
  @MethodSource("simpleNumberArgs")
  fun simpleNumberTest(src: String, tokens: List<Token<TokenType>>) {
    src.parse(simpleNumberTokenizer, TokenType.DEFAULT).assertEquals(tokens)
  }


/*  private val bracketsTokenizer = BracketsTokenizer(
      TokenType.BRACKETS,
      MultiTokenizer(
          TokenType.DEFAULT,
          listOf<ITokenizer<TokenType>>(
              WordTokenizer(TokenType.WORD),
              SingleCharTokenizer(TokenType.COMMA,','),
              WhitespaceTokenizer(TokenType.WHITESPACE)
          )
      ),
      TokenType.L_BR, TokenType.R_BR
  )

  fun bracketsArgs(): Stream<Arguments> =
      Stream.of(
          Arguments.of(
              "[xxx]",
              listOf(
                  ComplexToken(TokenType.BRACKETS, 0, 4,
                      listOf(
                          Token(TokenType.L_BR, 0, 0),
                          Token(TokenType.WORD, 1, 3),
                          Token(TokenType.R_BR, 4, 4)
                      )
                  )
              )
          ),
          Arguments.of(
              "[xxx, yyy]",
              listOf(
                  ComplexToken(TokenType.BRACKETS, 0, 9,
                      listOf(
                          Token(TokenType.L_BR, 0, 0),
                          Token(TokenType.WORD, 1, 3),
                          Token(TokenType.COMMA, 4, 4),
                          Token(TokenType.WHITESPACE, 5, 5),
                          Token(TokenType.WORD, 6, 8),
                          Token(TokenType.R_BR, 9, 9)
                      )
                  )
              )
          ),
          Arguments.of(
              "[xxx] [yyy]",
              listOf(
                  ComplexToken(TokenType.BRACKETS, 0, 4,
                      listOf(
                          Token(TokenType.L_BR, 0, 0),
                          Token(TokenType.WORD, 1, 3),
                          Token(TokenType.R_BR, 4, 4)
                      )
                  ),
                  Token(TokenType.WHITESPACE, 5, 5),
                  ComplexToken(TokenType.BRACKETS, 6, 10,
                      listOf(
                          Token(TokenType.L_BR, 6, 6),
                          Token(TokenType.WORD, 7, 9),
                          Token(TokenType.R_BR, 10, 10)
                      )
                  )
              )
          )
      )

  @ParameterizedTest(name = "BracketsTokenizer: \"{0}\"")
  @MethodSource("bracketsArgs")
  fun bracketsTest(src: String, tokens: List<Token<TokenType>>) {
    bracketsTokenizer.reset()
    src.parse(bracketsTokenizer, TokenType.WHITESPACE).assertEquals(tokens)
  }*/
}
