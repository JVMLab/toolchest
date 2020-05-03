package com.jvmlab.commons.parse.text


/**
 * Parent class of all the statuses of an [ITokenizer] returned by its functions.
 */
sealed class TokenizerStatus


/**
 * Initial status an [ITokenizer] before processing a first actual [Char] of a token.
 */
class StatusNone<E: Enum<E>>(private val tokenizer: IStartTokenizer<E>) : TokenizerStatus(),
    IStartTokenizer<E> by tokenizer {

  companion object ClassProps {
    const val name = "NONE"
  }

  override fun toString(): String = name
}


/**
 * Parent class of all the statuses of an [ITokenizer] which started tokenization process (all statuses
 * except [StatusNone]). [ModifiedStatus] can be changed later to some [FinalModifiedStatus],
 * when some other [Char] is processed.
 */
sealed class ModifiedStatus(private val subSequence: ISubSequence) : TokenizerStatus(), ISubSequence by subSequence


/**
 * Represents incomplete "in progress" status of an [ITokenizer]
 */
open class StatusBuilding<E: Enum<E>>(
    private val tokenizer: IProcessTokenizer<E>,
    subSequence: ISubSequence
    ) : ModifiedStatus(subSequence),
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IProcessTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(tokenizer, SubSequence(start, finish))


  companion object ClassProps {
    const val name = "BUILDING"
  }

  override fun toString(): String = name

  fun nextBuilding() = StatusBuilding<E>(tokenizer, start, finish + 1)

  fun thisFinish() = StatusFinished<E>(tokenizer, start, finish)

  fun nextFinish() = StatusFinished<E>(tokenizer, start, finish + 1)

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
class StatusFinished<E: Enum<E>>(
    private val tokenizer: IResetTokenizer<E>,
    subSequence: ISubSequence) : FinalModifiedStatus(subSequence),
    IResetTokenizer<E> by tokenizer {

  constructor(
      tokenizer: IResetTokenizer<E>,
      start: Int,
      finish: Int = start
  ) : this(tokenizer, SubSequence(start, finish))

  companion object ClassProps {
    const val name = "FINISHED"
  }

  override fun toString(): String = name

  fun createToken() = Token<E>(tokenizer.type, start, finish)
}


/**
 * Represents cancelled status of an [ITokenizer]. This status is supposed to be treated
 * as normal valid status in contrast to [StatusFailed]. It can be used, for example, in case
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


  companion object ClassProps {
    const val name = "CANCELLED"
  }

  override fun toString(): String = name
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
      finish: Int = start
  ) : this(tokenizer, SubSequence(start, finish))


  companion object ClassProps {
    val name = toString()
  }

  override fun toString(): String = "CANCELLED"
}


