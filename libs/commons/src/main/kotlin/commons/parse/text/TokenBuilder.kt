package com.jvmlab.commons.parse.text


/**
 * Represents a current status of a token to be built by a [TokenBuilder].
 */
sealed class BuildingStatus
object StatusNone : BuildingStatus() { override fun toString(): String = "NONE" }
object StatusBuilding : BuildingStatus() { override fun toString(): String = "BUILDING" }
object StatusFinished : BuildingStatus() { override fun toString(): String = "FINISHED" }
object StatusCancelled : BuildingStatus() { override fun toString(): String = "CANCELLED" }
data class StatusFailed(val reason: String = "") : BuildingStatus() { override fun toString(): String = "FAILED" }


/**
 * Represents an incomplete [Token] in progress and has build() method to build a completed [Token] when
 * [status] equals [StatusFinished]
 * Used in [AbstractTokenizer] to keep an intermediate result of the [AbstractTokenizer]
 * while constructing a [Token]. The class extends [Token] and adds some variable
 * properties to monitor the current status or the result of a [Token] creation
 *
 * @property type is overridden to variable and used as a [Token] type built by this [TokenBuilder]
 * @property start is overridden to variable, should be 0 for [StatusNone]
 * @property finish is overridden to variable, should be 0 for [StatusNone]
 * @property status represents the current status or the result of a [Token] creation
 * @property current keeps a read-only wrapper [RTokenBuilder] around this [TokenBuilder]
 */
class TokenBuilder<E: Enum<E>> (
    override var type: E,
    start: Int = 0,
    finish: Int = start,
    status: BuildingStatus = StatusNone) : Token<E>(type, start, finish) {

  override var start: Int = start
    set(value) {
      if (status == StatusNone) {
        require(value == 0) {
          "Incorrect start value ($value) for the BuildingStatus $StatusNone"
        }
      }
      require(value <= finish) {
        "Illegal attempt to set start value ($value) greater than finish ($finish)"
      }
      field = value
    }

  override var finish: Int = finish
    set(value) {
      if (status == StatusNone) {
        require(value == 0) {
          "Incorrect finish value ($value) for the BuildingStatus $StatusNone"
        }
      }
      require(value >= start) {
        "Illegal attempt to set finish value ($value) less than start ($start)"
      }
      field = value
    }

  var status: BuildingStatus = status
    set(value) {
      if (status == StatusNone) {
        require(start == 0) {
          "Incorrect start value ($start) for the BuildingStatus $StatusNone"
        }
        require(finish == 0) {
          "Incorrect finish value ($finish) for the BuildingStatus $StatusNone"
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
    status = StatusNone
  }


  /**
   * Sets [TokenBuilder] properties to start a new token
   */
  fun startToken(start: Int, status: BuildingStatus) {
    require(status != StatusNone) {
      "Incorrect status ($status) to start a token"
    }
    this.status = status
    this.finish = start
    this.start = start
  }


  /**
   * Builds a new [Token]
   * A [TokenBuilder] *MUST* have [StatusFinished] before colling this method
   *
   * @throws IllegalStateException when [TokenBuilder] has an improper [BuildingStatus]
   */
  fun build(): Token<E> {
    check(status == StatusFinished) {
      "Illegal attempt to build $type token with incorrect status: $status"
    }
    return Token<E>(type, start, finish)
  }

}