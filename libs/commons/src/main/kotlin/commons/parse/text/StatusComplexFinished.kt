package com.jvmlab.commons.parse.text


/**
 * Subclass of [StatusFinished] used by [StatusBuilding]. It creates [ComplexToken]
 */
class StatusComplexFinished<E: Enum<E>>(
    tokenizer: IResetTokenizer<E>,
    subSequence: ISubSequence,
    private val subTokens: List<Token<E>>) : StatusFinished<E>(tokenizer, subSequence) {
}