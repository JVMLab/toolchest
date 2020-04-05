package com.jvmlab.commons.parse.text


/**
 * A helper data class to keep sub-tokenizers of a [ComplexTokenizer]
 *
 * @property tokenizer is an [AbstractTokenizer] to keep
 * @property minTokens is a minimum number of tokens expected from [tokenizer]
 * @property maxTokens is a maximum number of tokens allowed for [tokenizer], 0 means unlimited
 */
data class SubTokenizer<E: Enum<E>>(
    val tokenizer: AbstractTokenizer<E>,
    val minTokens: Int = 0,
    val maxTokens: Int = 0) : ITokenizer<E> by tokenizer {

  init {
    require(minTokens >= 0) { "minTokens ($minTokens) should be >= 0" }
    require(maxTokens >= 0) { "minTokens ($maxTokens) should be >= 0" }
    require(minTokens <= maxTokens || maxTokens == 0) {
      "minTokens ($minTokens) should be <= maxTokens ($maxTokens)"
    }
  }
}


/**
 * An [AbstractTokenizer] which creates a [ComplexToken]. sub-tokens of the [ComplexToken]
 * are created by an internal a list of sub-tokenizers stored in a private [subTokenizers] property.
 *
 * @property subTokenizers a list of [SubTokenizer] to create sub-tokens
 * @property subTokens mutable list of tokens to be included in the resulting [ComplexToken]
 */
open class ComplexTokenizer<E: Enum<E>>(
    type: E,
    private val subTokenizers: List<SubTokenizer<E>>) : AbstractTokenizer<E>(type) {

  private var subTokens: MutableList<Token<E>> = ArrayList()
  private var subIdx: Int = 0
  private var tokenCount: Int = 0
  private var currentTokenizer: SubTokenizer<E> = subTokenizers.first()


  init {
    require(subTokenizers.isNotEmpty()) { "subTokenizers List cannot be empty" }
    subInit()
  }

  private fun subInit() {
    subTokens = ArrayList()
    subTokenizers.forEach { it.reset() }
    subIdx = 0
    tokenCount = 0
    currentTokenizer = subTokenizers.first()
  }


  /**
   * Resets all [subTokenizers] and clears [subTokens]
   */
  override fun reset() {
    super.reset()
    subInit()
  }


  /**
   * Builds a resulting token and resets [ComplexTokenizer], so a current token can be built
   * only once.
   *
   * @throws IllegalStateException when [tokenBuilder] has an improper [BuildingStatus]
   */
  override fun buildToken(): ComplexToken<E> {
    val token = ComplexToken(getRTokenBuilder().build(), subTokens)
    reset()
    return token
  }


  override fun firstChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails {
    currentTokenizer.processChar(char, idx, isLast)
    return analyzeCurrentTokenizer(char, idx, isLast) ?:
    BuildingDetails(BuildingStatus.NONE) // actually will never happen for firstChar()
  }


  override fun nextChar(char: Char, idx: Int, isLast: Boolean): BuildingDetails  {
    // we will return from the method in the middle of the loop in case of a successful or failed
    // char processing by some sub-tokenizer
    loop@ while (subIdx < subTokenizers.size) {
      currentTokenizer = subTokenizers[subIdx]

      // process char in the currentTokenizer if it is already in progress with some token
      // or it is allowed to start one more
      if (BuildingStatus.BUILDING == currentTokenizer.getBuildingStatus() ||
          currentTokenizer.maxTokens == 0 ||
          tokenCount < currentTokenizer.maxTokens) {
        currentTokenizer.processChar(char, idx, isLast)
      } else { // switch to a next tokenizer
        subIdx++
        tokenCount = 0
        continue@loop
      }

      // analyze processing results, return in case of failure or set charProcessed accordingly
      val type = currentTokenizer.getRTokenBuilder().type
      when (currentTokenizer.getBuildingStatus()) {
        BuildingStatus.BUILDING, // normal or failed returns are mostly made from here
        BuildingStatus.FAILED,
        BuildingStatus.CANCELLED,
        BuildingStatus.FINISHED -> {
          return analyzeCurrentTokenizer(char, idx, isLast) ?:
          continue@loop // to try one more time with the same sub-tokenizer
        }
        BuildingStatus.NONE -> { // switch to a next sub-tokenizer if possible or break the loop
          if (tokenCount >= currentTokenizer.minTokens) {// allows to switch to a next sub-tokenizer
            subIdx++
            tokenCount = 0
          } else return BuildingDetails(BuildingStatus.FAILED,
              "$type sub-tokenizer could not match a token at position $idx")
        }
      }
    }

    // at this stage we didn't processed the char and reached the last tokenizer
    finalCharIncluded = false
    return BuildingDetails(BuildingStatus.FINISHED)
  }


  /**
   * Implements common logic of [firstChar] and [nextChar]
   */
  private fun analyzeCurrentTokenizer(char: Char, idx: Int, isLast: Boolean): BuildingDetails? {
    val type = currentTokenizer.getRTokenBuilder().type
    return when (currentTokenizer.getBuildingStatus()) {
      BuildingStatus.NONE,
      BuildingStatus.BUILDING, // normal or failed returns are mostly made from here
      BuildingStatus.FAILED -> return currentTokenizer.getRTokenBuilder().details
      BuildingStatus.CANCELLED -> return BuildingDetails(BuildingStatus.FAILED,
          "Cancelled sub-tokenizer $type at position $idx")
      BuildingStatus.FINISHED -> {
        val charProcessed = (currentTokenizer.getRTokenBuilder().finish == idx)
        subTokens.add(currentTokenizer.buildToken())
        tokenCount++
        // we have to return from here and carefully set the correct status
        if (charProcessed) {
          if (isLast) {
            if ((tokenCount >= currentTokenizer.minTokens) &&
                ((subIdx == subTokenizers.size - 1) ||
                    !subTokenizers.slice((subIdx + 1)..(subTokenizers.size - 1)).any {
                      it.minTokens > 0
                    })) {
              finalCharIncluded = true
              BuildingDetails(BuildingStatus.FINISHED)
            } else BuildingDetails(BuildingStatus.FAILED,
                "Unexpected end of CharSequence at position $idx for $defaultTokenType")
          } else BuildingDetails(BuildingStatus.BUILDING) // greedy behaviour
        } else null
      }
    }
  }
}