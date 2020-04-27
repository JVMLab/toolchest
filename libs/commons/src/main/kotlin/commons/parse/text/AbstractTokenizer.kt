package com.jvmlab.commons.parse.text


/**
 * TODO
 */
abstract class AbstractTokenizer<E: Enum<E>> (override var type: E) : IModifiableTokenizer<E> {

  /**
   * Resets an [AbstractTokenizer] to an initial state.
   *
   * The standard implementation just returns [StatusNone] with the same [AbstractTokenizer] assuming
   * it doesn't hold any state. An implementation in a sub-class which holds a state must override this
   * function to reset the internal state
   */
  override fun reset() = StatusNone(this)
}