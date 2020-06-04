package com.jvmlab.commons.parse.lexer.tokenizers

import com.jvmlab.commons.parse.lexer.statuses.*


/**
 * Contains functions to start a new tokenization.
 */
interface IStartTokenizer<E: Enum<E>> : IResetTokenizer<E>  {
  /**
   * Starts a new tokenization. Resets [ITokenizer] to an initial state before processing a [Char].
   * For a last [Char] from a [CharSequence] the [startProcessingLast] should be called instead.
   *
   * @param char is a [Char] to be parsed
   * @param start is a position of the [char] in a parsed [CharSequence]
   */
  fun startProcessing(char: Char, start: Int = 0) : ModifiedStatus


  /**
   * Starts a new tokenization for the last [Char] of a [CharSequence]. Resets [ITokenizer] to an initial
   * state before processing a [Char]. It returns [FinalModifiedStatus] because tokenization must be
   * finished by the end of the [CharSequence].
   *
   * @param char is a [Char] to be parsed
   * @param start is a position of the [char] in a parsed [CharSequence]
   */
  fun startProcessingLast(char: Char, start: Int = 0) : FinalModifiedStatus


  /**
   * Resets an [IStartTokenizer] to an initial state.
   *
   * The standard implementation just returns [StatusNone] with the same [IStartTokenizer] assuming
   * it doesn't hold any state. An implementation in a sub-class which holds a state must override this
   * function to reset the internal state
   */
  override fun reset() = StatusNone(this)
}
