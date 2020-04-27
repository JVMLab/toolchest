package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
    assertTrue(tokenizer.startProcessing(' ', 0) is StatusNone<*>)
  }

  @Test
  fun `non matching last`() {
    assertTrue(tokenizer.startProcessingLast(' ', 0) is StatusNone<*>)
  }

  @Test
  fun `matching not last`() {
    val status = tokenizer.startProcessing('$', 0)
    status.assertFinish()

    if (status is StatusFinished<*>)
      status.createToken().assertEquals(expectedToken)
  }

  @Test
  fun `matching last`() {
    val status = tokenizer.startProcessingLast('$', 0)
    status.assertFinish()

    if (status is StatusFinished<*>)
      status.createToken().assertEquals(expectedToken)
  }
}
