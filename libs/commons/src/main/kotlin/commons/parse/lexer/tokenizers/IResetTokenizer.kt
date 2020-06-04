package com.jvmlab.commons.parse.lexer.tokenizers

import com.jvmlab.commons.parse.lexer.statuses.*


/**
 *  Used to reset [ITokenizer] to the initial state
 */
interface IResetTokenizer<E: Enum<E>> : ITokenizerType<E> {
  /**
   * Resets an [ITokenizer] to an initial state.
   */
  fun reset() : StatusNone<E>
}
