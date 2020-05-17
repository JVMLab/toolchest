package com.jvmlab.commons.parse.text.statuses

import com.jvmlab.commons.parse.text.subsequence.*
import com.jvmlab.commons.parse.text.tokenizers.*


/**
 * Subclass of [StatusBuilding] to keep state of [AlternativeTokenizer]
 */
class StatusAltBuilding<E: Enum<E>> private constructor(
    tokenizer: IProcessTokenizer<E>,
    subSequence: ISubSequence,
    private val activeStatuses: MutableList<StatusBuilding<E>>
) : StatusBuilding<E>(tokenizer, subSequence),
    MutableList<StatusBuilding<E>> by activeStatuses {

  constructor(tokenizer: IProcessTokenizer<E>, start: Int, activeStatuses: MutableList<StatusBuilding<E>>) :
      this(tokenizer, VariableSubSequence(start), activeStatuses)


  internal fun maxCancelled(maxCancelledFinish: Int) : StatusCancelled<E> =
      if (maxCancelledFinish > finish)
        nextCancelled()
      else
        thisCancelled()
}
