package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
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

  @ParameterizedTest(name = "SingleCharTokenizer: \"{0}\"")
  @MethodSource("singleCharArgs")
  fun singleCharTest(src: String, tokens: List<Token<TokenType>>) {
    singleCharTokenizer.reset()
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
          )
      )

  @ParameterizedTest(name = "WordTokenizer: \"{0}\"")
  @MethodSource("wordArgs")
  fun wordTest(src: String, tokens: List<Token<TokenType>>) {
    wordTokenizer.reset()
    src.parse(wordTokenizer, TokenType.WHITESPACE).assertEquals(tokens)
  }
}