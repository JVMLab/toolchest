package com.jvmlab.commons.parse.lexer.tokenizers

import com.jvmlab.commons.parse.lexer.statuses.*


/**
 *  The [ITokenizer] implementation which processes each [Char] in parallel in a list of [altTokenizers].
 *  The only one tokenizer from this list should produce [StatusFinished], and the others should have
 *  [StatusCancelled]. Otherwise [AlternativeTokenizer] gives [StatusFailed] of [type]
 *
 *  [AlternativeTokenizer] is stateless, it uses [StatusAltBuilding] to keep intermediate state
 *
 *  Important: reset() call of [StatusCancelled] or [StatusFinished] gives [StatusNone] with one of
 *  [altTokenizers] inside! Don't use it if you want to continue tokenization using [AlternativeTokenizer],
 *  use reset() from [AlternativeTokenizer] instead.
 *
 *  @property type is the type of [StatusAltBuilding] or [StatusFailed]
 *  @property altTokenizers is the list of alternative tokenizers to process chars
 */
class AlternativeTokenizer<E: Enum<E>>(
    override val type: E,
    private val altTokenizers: List<IStartTokenizer<E>>
) : ITokenizer<E> {

  private fun failedAmbiguous(s1: ModifiedStatus, s2: ModifiedStatus, start: Int) : StatusFailed<E> =
      StatusFailed(
          this,
          start,
          "Ambiguous: $s1 and $s2 are found at the same position $start"
      )


  override fun startProcessing(char: Char, start: Int): ModifiedStatus {
    val buildingStatuses: MutableList<StatusBuilding<E>> = ArrayList()
    var statusFinished: StatusFinished<E>? = null

    loop@ for (altTokenizer in altTokenizers)

      when (val status = altTokenizer.startProcessing(char, start)) {
        is StatusBuilding<*>  ->
          if (null == statusFinished)
            @Suppress("UNCHECKED_CAST")
            buildingStatuses.add(status as StatusBuilding<E>)
          else
            return failedAmbiguous(status, statusFinished, start)
        is StatusFinished<*> ->
          if (buildingStatuses.isEmpty())
            if (null == statusFinished) {
              @Suppress("UNCHECKED_CAST")
              statusFinished = status as StatusFinished<E>
            } else
              return failedAmbiguous(status, statusFinished, start)
          else
            return failedAmbiguous(status, buildingStatuses.first(), start)
        is StatusCancelled<*> -> // just skip it
          continue@loop
        is StatusFailed<*> ->
          return status
      }

    if (null != statusFinished)
      return statusFinished

    if (buildingStatuses.isEmpty())  // all altTokenizers returned StatusCancelled
      return StatusCancelled(this, start)

    if (buildingStatuses.size == 1)
      return buildingStatuses.first()

    return StatusAltBuilding<E>(this, start, buildingStatuses)
  }


  override fun startProcessingLast(char: Char, start: Int): FinalModifiedStatus {
    var statusFinished: StatusFinished<E>? = null

    loop@ for (altTokenizer in altTokenizers)
      when (val status = altTokenizer.startProcessingLast(char, start)) {
        is StatusFinished<*> ->
          if (null == statusFinished) {
            @Suppress("UNCHECKED_CAST")
            statusFinished = status as StatusFinished<E>
          } else
            return failedAmbiguous(status, statusFinished, start)
        is StatusCancelled<*> -> // just skip it
          continue@loop
        is StatusFailed<*> ->
          return status
      }

    return statusFinished ?: StatusCancelled(this, start)
  }


  override fun processChar(char: Char, lastStatus: StatusBuilding<E>): ModifiedStatus {
    val lastAltBuilding: StatusAltBuilding<E> =
        (lastStatus as? StatusAltBuilding<E>) ?: return lastStatus.processChar(char)
    var statusFinished: StatusFinished<E>? = null

    val iterator = lastAltBuilding.listIterator()
    var maxCancelledFinish = lastAltBuilding.finish
    while (iterator.hasNext())
      when (val status = iterator.next().processChar(char)) {
        is StatusBuilding<*> -> { // keep in lastAltBuilding
          @Suppress("UNCHECKED_CAST")
          iterator.set(status as StatusBuilding<E>)
        }
        is StatusFinished<*> ->
          if (null == statusFinished) {
            @Suppress("UNCHECKED_CAST")
            statusFinished = status as StatusFinished<E>
          } else
            return failedAmbiguous(status, statusFinished, status.finish)
        is StatusCancelled<*> -> {
          maxCancelledFinish =
              if (status.finish > maxCancelledFinish)
                status.finish
              else
                maxCancelledFinish
          iterator.remove()
        }
        is StatusFailed<*> ->
          return status
      }

    if (null != statusFinished)
      return statusFinished

    if (lastAltBuilding.isEmpty())  // all lastAltBuilding.processChar(char) returned StatusCancelled
      return lastAltBuilding.maxCancelled(maxCancelledFinish)

    if (lastAltBuilding.size == 1)
      return lastAltBuilding.first()

    return lastAltBuilding.nextBuilding()
  }


  override fun processLastChar(char: Char, lastStatus: StatusBuilding<E>): FinalModifiedStatus {
    var statusFinished: StatusFinished<E>? = null
    val lastAltBuilding: StatusAltBuilding<E> =
        (lastStatus as? StatusAltBuilding<E>) ?: return lastStatus.processLastChar(char)

    val iterator = lastAltBuilding.listIterator()
    var maxCancelledFinish = lastAltBuilding.finish
    while (iterator.hasNext())
      when (val status = iterator.next().processLastChar(char)) {
        is StatusFinished<*> ->
          if (null == statusFinished) {
            @Suppress("UNCHECKED_CAST")
            statusFinished = status as StatusFinished<E>
          } else
            return failedAmbiguous(status, statusFinished, status.finish)
        is StatusCancelled<*> -> {
          maxCancelledFinish =
              if (status.finish > maxCancelledFinish)
                status.finish
              else
                maxCancelledFinish
          iterator.remove()
        }
        is StatusFailed<*> ->
          return status
      }

    return statusFinished ?: lastAltBuilding.maxCancelled(maxCancelledFinish)
  }


  /**
   * Makes [reset] call of all [altTokenizers] just in case they are not stateless
   */
  override fun reset(): StatusNone<E> {
    altTokenizers.forEach { it.reset() }
    return super.reset()
  }
}
