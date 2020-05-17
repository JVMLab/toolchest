package com.jvmlab.commons.parse.text.tokenizers

import com.jvmlab.commons.parse.text.Token
import com.jvmlab.commons.parse.text.statuses.*


/**
 * An [ITokenizer] to create a [ComplexToken]. sub-tokens of the [ComplexToken]
 * are created by an internal a list of sub-tokenizers stored in a private [subTokenizers] property.
 *
 * [ComplexTokenizer] is stateless, it uses [StatusComplexBuilding] to keep intermediate state
 *
 * @property subTokenizers a list of [SubTokenizer] to create sub-tokens
 */
open class ComplexTokenizer<E: Enum<E>>(
    override val type: E,
    private val subTokenizers: List<SubTokenizer<E>>
) : ITokenizer<E> {

  init {
    require(subTokenizers.isNotEmpty()) { "subTokenizers list cannot be empty" }
  }


  private fun failedIncomplete(start: Int, finish: Int = start) =
      StatusFailed(
          this,
          start,
          finish,
          "Couldn't find find all required sub-tokens for $type started at $start by the end of CharSequence"
      )


  override fun startProcessing(char: Char, start: Int): ModifiedStatus {
    val subTokens = ArrayList<Token<E>>()

    with(SubStatus.new(subTokenizers)) {
      // loop over subTokenizers trying to find a sub-tokenizer which is able to process char
      do {
        when (val analyzedStatus = status) {
          is StatusNone<*> ->
            status = analyzedStatus.startProcessing(char, start)
          is StatusBuilding<*> ->
            return StatusComplexBuilding(this@ComplexTokenizer, start, this, subTokens)
          is StatusFinished<*> -> {
            @Suppress("UNCHECKED_CAST")
            subTokens.add(analyzedStatus.createToken() as Token<E>)
            tokenCount++
            return if (canFinish())
              StatusComplexFinished(this@ComplexTokenizer, start, subTokens)
            else
              if (canCreateMoreTokens())
                StatusComplexBuilding(this@ComplexTokenizer, start, reset(), subTokens)
              else // we have next() sub-tokenizer otherwise the above call to canFinish() would return true
                StatusComplexBuilding(this@ComplexTokenizer, start, next(), subTokens)
          }
          is StatusCancelled<*> ->
            if (canTakeNext())
              // continue to the next sub-tokenizer because the current couldn't process char and it's not required
              next()
            else
              // return StatusCancelled if some token is required
              return StatusCancelled(this@ComplexTokenizer, start)
          is StatusFailed<*> ->
            @Suppress("UNCHECKED_CAST")
            return status as StatusFailed<E>
        }
      } while (true)
    }
  }


  override fun startProcessingLast(char: Char, start: Int): FinalModifiedStatus {
    val subTokens = ArrayList<Token<E>>()

    with(SubStatus.new(subTokenizers)) {
      // loop over subTokenizers trying to find a sub-tokenizer which is able to process char
      do {
        when (val analyzedStatus = status) {
          is StatusNone<*> ->
            status = analyzedStatus.startProcessingLast(char, start)
          is StatusBuilding<*> ->
            throw IllegalStateException("Unexpected $status at position $start")
          is StatusFinished<*> -> {
            @Suppress("UNCHECKED_CAST")
            subTokens.add(analyzedStatus.createToken() as Token<E>)
            tokenCount++
            return if (canFinish())
              StatusComplexFinished(this@ComplexTokenizer, start, subTokens)
            else
              failedIncomplete(start)
          }
          is StatusCancelled<*> ->
            if (canTakeNext())
              // continue to the next sub-tokenizer because the current couldn't process char and it's not required
              next()
            else
              // return StatusCancelled if some token is required
              return StatusCancelled(this@ComplexTokenizer, start)
          is StatusFailed<*> ->
            @Suppress("UNCHECKED_CAST")
            return status as StatusFailed<E>
        }
      } while (true)
    }
  }


  override fun processChar(char: Char, lastStatus: StatusBuilding<E>): ModifiedStatus {
    val lastComplexBuilding: StatusComplexBuilding<E> =
        (lastStatus as? StatusComplexBuilding<E>) ?: return lastStatus.processChar(char)

    with(lastComplexBuilding.subStatus) {
      // 2-cycle loop: the first cycle processes a char, the second returns the result
      do {
        when (val analyzedStatus = status) {
          is StatusNone<*> -> {
            status = analyzedStatus.startProcessing(char, lastComplexBuilding.finish + 1)
            if (status is StatusBuilding<*>)
              return lastComplexBuilding.nextBuilding()
          }
          is StatusBuilding<*> -> {
            status = analyzedStatus.processChar(char)
            if (status is StatusBuilding<*>)
              return lastComplexBuilding.nextBuilding()
          }
          is StatusFinished<*> -> {
            @Suppress("UNCHECKED_CAST")
            lastComplexBuilding.add(analyzedStatus.createToken() as Token<E>)
            tokenCount++
            if (canFinish())
              return lastComplexBuilding.rightFinish(status as StatusFinished<*>)
            else {
              if (canCreateMoreTokens())
                reset()
              else
                next()
              if (analyzedStatus.finish > lastComplexBuilding.finish)
                return lastComplexBuilding.nextBuilding()
            }
          }
          is StatusCancelled<*> ->
            if (lastComplexBuilding.isEmpty())
               return lastComplexBuilding.rightCancelled(status as StatusCancelled<*>)
            else
              if (analyzedStatus.finish == lastComplexBuilding.finish)
                reset() // try one mode time with the same sub-tokenizer
              else {
                if (canTakeNext())
                  next() // try one mode time with the next sub-tokenizer
                else
                  return failedIncomplete(lastComplexBuilding.start, analyzedStatus.finish)
              }
          is StatusFailed<*> ->
            @Suppress("UNCHECKED_CAST")
            return status as StatusFailed<E>
        }
      } while (true)
    }
  }


  override fun processLastChar(char: Char, lastStatus: StatusBuilding<E>): FinalModifiedStatus {
    val lastComplexBuilding: StatusComplexBuilding<E> =
        (lastStatus as? StatusComplexBuilding<E>) ?: return lastStatus.processLastChar(char)

    with(lastComplexBuilding.subStatus) {
      // 2-cycle loop: the first cycle processes a char, the second returns the result
      do {
        when (val analyzedStatus = status) {
          is StatusNone<*> -> {
            status = analyzedStatus.startProcessingLast(char, lastComplexBuilding.finish + 1)
          }
          is StatusBuilding<*> -> {
            status = analyzedStatus.processLastChar(char)
          }
          is StatusFinished<*> -> {
            @Suppress("UNCHECKED_CAST")
            lastComplexBuilding.add(analyzedStatus.createToken() as Token<E>)
            tokenCount++
            if (canFinish())
              return lastComplexBuilding.rightFinish(status as StatusFinished<*>)
            else
              if (analyzedStatus.finish == lastComplexBuilding.finish)
                if (canCreateMoreTokens())
                  reset()
                else
                  next()
              else
                return failedIncomplete(lastComplexBuilding.start, analyzedStatus.finish)
          }
          is StatusCancelled<*> ->
            if (lastComplexBuilding.isEmpty())
              return lastComplexBuilding.rightCancelled(status as StatusCancelled<*>)
            else
              if (analyzedStatus.finish == lastComplexBuilding.finish)
                reset() // try one mode time with the same sub-tokenizer
              else
                if (canTakeNext())
                  next() // try one mode time with the next sub-tokenizer
                else
                  return failedIncomplete(lastComplexBuilding.start, analyzedStatus.finish)
          is StatusFailed<*> ->
            @Suppress("UNCHECKED_CAST")
            return status as StatusFailed<E>
        }
      } while (true)
    }
  }

  /**
   * Makes [reset] call of all [subTokenizers] just in case they are not stateless
   */
  override fun reset(): StatusNone<E> {
    subTokenizers.forEach { it.reset() }
    return super.reset()
  }
}
