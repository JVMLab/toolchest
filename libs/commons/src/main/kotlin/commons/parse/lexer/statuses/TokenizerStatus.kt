package com.jvmlab.commons.parse.lexer.statuses

import com.jvmlab.commons.parse.lexer.Token
import com.jvmlab.commons.parse.lexer.subsequence.*
import com.jvmlab.commons.parse.lexer.tokenizers.*


/**
 * Parent class of all the statuses of an [ITokenizer] returned by its functions.
 */
sealed class TokenizerStatus


/**
 * Initial status an [ITokenizer] before processing a first actual [Char] of a token.
 */
class StatusNone<E: Enum<E>>(private val tokenizer: IStartTokenizer<E>) : TokenizerStatus(),
    IStartTokenizer<E> by tokenizer {

  companion object {
    const val name = "NONE"
  }

  override fun toString(): String = "$name of ${tokenizer.type}"
}


/**
 * Parent class of all the statuses of an [ITokenizer] which started tokenization process (all statuses
 * except [StatusNone]). [ModifiedStatus] can be changed later to some [FinalModifiedStatus],
 * when some other [Char] is processed.
 */
sealed class ModifiedStatus(protected val subSequence: ISubSequence) :
    TokenizerStatus(), ISubSequence by subSequence


/**
 * Represents incomplete "in progress" status of an [ITokenizer]
 */
open class StatusBuilding<E: Enum<E>> protected constructor(
    protected val tokenizer: IProcessTokenizer<E>,
    subSequence: ISubSequence
    ) : ModifiedStatus(subSequence),
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IProcessTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(tokenizer, VariableSubSequence(start, finish))


  companion object {
    const val name = "BUILDING"
  }

  override fun toString(): String = "$name of ${tokenizer.type}"

  internal fun nextBuilding(): StatusBuilding<E> {
    subSequence.stretch()
    return this
  }

  internal open fun thisFinish() : StatusFinished<E> = StatusFinished(tokenizer, subSequence)

  internal open fun nextFinish() : StatusFinished<E> = StatusFinished(tokenizer, subSequence.stretch())

  internal fun thisCancelled() : StatusCancelled<E> = StatusCancelled(tokenizer, subSequence)

  internal fun nextCancelled() : StatusCancelled<E> = StatusCancelled(tokenizer, subSequence.stretch())

  fun processChar(char: Char): ModifiedStatus = tokenizer.processChar(char, this)

  fun processLastChar(char: Char): FinalModifiedStatus = tokenizer.processLastChar(char, this)
}


/**
 * Parent class of all the [ModifiedStatus] statuses of an [ITokenizer] except incomplete status [StatusBuilding].
 */
sealed class FinalModifiedStatus(subSequence: ISubSequence) : ModifiedStatus(subSequence)


/**
 * This is the only successful completion status which can create a [Token].
 */
open class StatusFinished<E: Enum<E>>(
    private val tokenizer: IResetTokenizer<E>,
    subSequence: ISubSequence) : FinalModifiedStatus(subSequence),
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(tokenizer, SubSequence(start, finish))

  companion object {
    const val name = "FINISHED"
  }

  override fun toString(): String = "$name of ${tokenizer.type}"

  open fun createToken() = Token(tokenizer.type, start, finish)
}


/**
 * Represents cancelled status of an [ITokenizer]. This status is supposed to be treated
 * as normal valid status in contrast to [StatusFailed]. It is used when no token ws crated after
 * starting processing from [StatusNone]. It can be used also, for example, in case
 * of tokenization of a [CharSequence] to one of several possible keywords like 'max' and 'maximum'.
 * [ITokenizer] for 'max' will have [StatusCancelled] after 'i' [Char] processing, while
 * [ITokenizer] for 'maximum' will still have [StatusBuilding]
 */
class StatusCancelled<E: Enum<E>>(
    private val tokenizer: IResetTokenizer<E>,
    subSequence: ISubSequence) : FinalModifiedStatus(subSequence),
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(tokenizer, SubSequence(start, finish))


  companion object {
    const val name = "CANCELLED"
  }

  override fun toString(): String = "$name of ${tokenizer.type}"
}


/**
 * Represents invalid status of an [ITokenizer], for example, in case of an unexpected [Char]
 * found inside brackets. In other words, [StatusFailed] is an indication of a parsing error.
 *
 * @property reason is an optional reason describing what has caused the [StatusFailed]
 */
class StatusFailed<E: Enum<E>>(
    private val tokenizer: IResetTokenizer<E>,
    subSequence: ISubSequence,
    val reason: String = "") : FinalModifiedStatus(subSequence),
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      finish: Int,
      reason: String = ""
  ) : this(tokenizer, SubSequence(start, finish), reason)

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      reason: String = ""
  ) : this(tokenizer, SubSequence(start, start), reason)


  companion object {
    const val name = "FAILED"
  }

  override fun toString(): String = "$name of ${tokenizer.type}"
}


