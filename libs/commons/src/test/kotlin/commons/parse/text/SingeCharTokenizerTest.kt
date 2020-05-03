package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.BeforeEach


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SingeCharTokenizerTest {
  private val tokenizer = SingleCharTokenizer(TokenType.DEFAULT, '$')
  private val expectedToken = Token(TokenType.DEFAULT, 0, 0)

  @BeforeEach
  fun reset() {
    tokenizer.reset()
  }

  @Test
  fun `non matching not last`() {
    tokenizer.startProcessing(' ', 0)
        .assertCancelled().reset()
        .assertNone()
  }

  @Test
  fun `non matching last`() {
    tokenizer.startProcessingLast(' ', 0)
        .assertCancelled().reset()
        .assertNone()
  }

  @Test
  fun `matching not last`() {
    tokenizer.startProcessing('$', 0)
        .assertFinish().createToken()
        .assertEquals(expectedToken)
  }

  @Test
  fun `matching last`() {
    tokenizer.startProcessingLast('$', 0)
        .assertFinish().createToken()
        .assertEquals(expectedToken)
  }

}
