package com.jvmlab.commons.parse.text


/**
 * Parent class of all the statuses of an [ITokenizer] returned by its functions.
 */
sealed class TokenizerStatus

/**
 * Parent class of all the statuses of an [ITokenizer] which cannot be changed to any
 * status except initial [StatusNone].
 */
sealed class FinalStatus : TokenizerStatus()

/**
 * Parent class of all the statuses of an [ITokenizer] which represent tokenization
 * in-progress. Such a status can be changed later to some [FinalStatus], when some
 * other [Char] is processed.
 */
sealed class ProgressStatus : TokenizerStatus()


/**
 * Represents incomplete "in progress" status of an [ITokenizer]
 */
open class StatusBuilding<E: Enum<E>>(
    private val subSequence: SubSequence,
    private val tokenizer: IProcessTokenizer<E>) : ProgressStatus(),
    ISubSequence by subSequence,
    IProcessTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IProcessTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(SubSequence(start, finish), tokenizer)

  override fun toString(): String = "BUILDING"

  override fun processChar(char: Char, lastStatus: StatusBuilding<E>): TokenizerStatus =
      tokenizer.processChar(char, this)

  override fun processLastChar(char: Char, lastStatus: StatusBuilding<E>): FinalStatus =
      tokenizer.processLastChar(char, this)
}


/**
 * Initial status an [ITokenizer] before processing a first actual [Char] of a token.
 */
class StatusNone<E: Enum<E>>(private val tokenizer: IStartTokenizer<E>) : FinalStatus(),
    IStartTokenizer<E> by tokenizer {
  override fun toString(): String = "NONE"
}


/**
 * This is the only successful completion status which can create a [Token].
 */
class StatusFinished<E: Enum<E>>(
    private val subSequence: SubSequence,
    private val tokenizer: IResetTokenizer<E>) : FinalStatus(),
    ISubSequence by subSequence,
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(SubSequence(start, finish), tokenizer)

  override fun toString(): String = "FINISHED"

  fun createToken() = Token<E>(tokenizer.type, subSequence)
}


/**
 * Represents cancelled status of an [ITokenizer]. This status is supposed to be treated
 * as normal valid status in contrast to [StatusFailed]. It can be used, for example, in case
 * of tokenization of a [CharSequence] to one of several possible keywords like 'max' and 'maximum'.
 * [ITokenizer] for 'max' will have [StatusCancelled] after 'i' [Char] processing, while
 * [ITokenizer] for 'maximum' will still have [StatusBuilding]
 */
class StatusCancelled<E: Enum<E>>(
    private val subSequence: SubSequence,
    private val tokenizer: IResetTokenizer<E>) : FinalStatus(),
    ISubSequence by subSequence,
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(SubSequence(start, finish), tokenizer)

  override fun toString(): String = "CANCELLED"
}


/**
 * Represents invalid status of an [ITokenizer], for example, in case of an unexpected [Char]
 * found inside brackets. In other words, [StatusFailed] is an indication of a parsing error.
 *
 * @property reason is an optional reason describing what has caused the [StatusFailed]
 */
class StatusFailed<E: Enum<E>>(
    private val subSequence: SubSequence,
    private val tokenizer: IResetTokenizer<E>,
    val reason: String = "") : FinalStatus(),
    ISubSequence by subSequence,
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(SubSequence(start, finish), tokenizer)

  override fun toString(): String = "FAILED"
}


