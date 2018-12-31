package com.jvmlab.commons.parse.text


/**
 * Represents a current status of a token to be built by a [TokenBuilder]. Used in [BuildingDetails]
 */
enum class BuildingStatus {
  NONE,       // a token is not yet started
  BUILDING,   // a normal status of a token in progress
  FINISHED,   // a successful completion status, the token can be built by build() call
  CANCELLED,  // a normal status when a current Char indicates that doesn't match the token,
              // and the token is not complete yet. Example: keywords
  FAILED      // an exceptional status in case of an unexpected current Char
}


/**
 * Holds a [Token] building details in a [TokenBuilder] or [AbstractTokenizer]
 *
 * @property status is the status of the token to be built
 * @property reason keeps a text message describing a reason of the [status] value, if any
 */
data class BuildingDetails (
    val status: BuildingStatus = BuildingStatus.NONE,
    val reason: String = "")


/**
 * Used in [AbstractTokenizer] to keep an intermediate result of the [AbstractTokenizer]
 * while constructing a [Token]. The class extends [Token] and adds some variable
 * properties to monitor the current status or the result of a [Token] creation
 *
 * @property start is overridden to variable, should be 0 for [BuildingStatus.NONE] of [details]
 * @property finish is overridden to variable, should be 0 for [BuildingStatus.NONE] of [details]
 * @property details represents the current status or the result of a [Token] creation
 * @property current keeps a read-only wrapper [RTokenBuilder] around this [TokenBuilder]
 */
class TokenBuilder<E: Enum<E>> (
    override var type: E,
    start: Int = 0,
    finish: Int = start,
    details: BuildingDetails = BuildingDetails()) : Token<E>(type, start, finish) {

  override var start: Int = start
    set(value) {
      if (BuildingStatus.NONE == details.status) {
        require(value == 0) {
          "Incorrect start value ($value) for the BuildingStatus ${BuildingStatus.NONE}"
        }
      }
      require(value <= finish) {
        "Illegal attempt to set start value ($value) greater than finish ($finish)"
      }
      field = value
    }

  override var finish: Int = finish
    set(value) {
      if (BuildingStatus.NONE == details.status) {
        require(value == 0) {
          "Incorrect finish value ($value) for the BuildingStatus ${BuildingStatus.NONE}"
        }
      }
      require(value >= start) {
        "Illegal attempt to set finish value ($value) less than start ($start)"
      }
      field = value
    }

  var details: BuildingDetails = details
    set(value) {
      if (BuildingStatus.NONE == details.status) {
        require(start == 0) {
          "Incorrect start value ($start) for the BuildingStatus ${BuildingStatus.NONE}"
        }
        require(finish == 0) {
          "Incorrect finish value ($finish) for the BuildingStatus ${BuildingStatus.NONE}"
        }
      }
      field = value
    }

  val current = RTokenBuilder<E>(this)


  /**
   * Resets this [TokenBuilder] to the default values and the initial [type]
   */
  fun reset() {
    type = super.type
    start = 0
    finish = 0
    details = BuildingDetails()
  }


  /**
   * Sets [TokenBuilder] properties to start a new token
   */
  fun startToken(start: Int, details: BuildingDetails) {
    require(details.status != BuildingStatus.NONE) {
      "Incorrect status (${details.status}) to start a token"
    }
    this.details = details
    this.finish = start
    this.start = start
  }


  /**
   * Builds a new [Token]
   * A [TokenBuilder] *MUST* have [BuildingStatus.FINISHED] of [details] before colling this method
   *
   * @throws IllegalStateException when [TokenBuilder] has an improper [BuildingStatus]
   */
  fun build(): Token<E> {
    check(BuildingStatus.FINISHED == details.status) {
      "Illegal attempt to build $type token with incorrect status: ${details.status}"
    }
    return Token<E>(type, start, finish)
  }

}