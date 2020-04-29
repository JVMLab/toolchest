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
    tokenizer.startProcessing(' ', 0).assertNone()
  }

  @Test
  fun `non matching last`() {
    tokenizer.startProcessingLast(' ', 0).assertNone()
  }

  @Test
  fun `matching not last`() {
    tokenizer.startProcessing('a', 0).assertBuilding {
      it.processChar(' ', it).assertFinish {
        it.createToken().assertEquals(expectedToken0)
      }
    }
  }

  @Test
  fun `matching last`() {
    tokenizer.startProcessing('a', 0).assertBuilding {
      it.processLastChar('b', it).assertFinish {
        it.createToken().assertEquals(expectedToken1)
      }
    }
  }

  @Test
  fun `matching last space`() {
    tokenizer.startProcessing('a', 0).assertBuilding {
      it.processLastChar(' ', it).assertFinish {
        it.createToken().assertEquals(expectedToken0)
      }
    }
  }

}
