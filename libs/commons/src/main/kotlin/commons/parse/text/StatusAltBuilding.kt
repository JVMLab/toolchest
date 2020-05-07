package com.jvmlab.commons.parse.text


/**
 * Subclass of [StatusBuilding] to keep state of [AlternativeTokenizer]
 */
class StatusAltBuilding<E: Enum<E>>(
    tokenizer: IProcessTokenizer<E>,
    subSequence: IStretchableSubSequence,
    private val activeStatuses: MutableList<StatusBuilding<E>>
) : StatusBuilding<E>(tokenizer, subSequence),
    MutableList<StatusBuilding<E>> by activeStatuses {

  internal fun maxCancelled(maxCancelledFinish: Int) : StatusCancelled<E> =
      if (maxCancelledFinish > finish)
        nextCancelled()
      else
        thisCancelled()
}