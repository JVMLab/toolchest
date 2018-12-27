package com.jvmlab.commons.parse.text


/**
 * Represents a current status of a token to be built by a [TokenBuilder]. Used in [TokenBuilder]
 * and in [AbstractTokenizer]
 */
enum class BuildingStatus {
  NONE,       // to be used in an AbstractTokenizer only when no TokenBuilder is created yet
  BUILDING,   // a normal status of a token in progress
  FINISHED,   // a successful completion status, the token can be built by build() call
  CANCELLED,  // a normal status when a current Char indicates that doesn't match the token,
              // and the token is not complete yet. Example: keywords
  FAILED      // an exceptional status in case of an unexpected current Char
}


/**
 * Used in [AbstractTokenizer] as a mutable returned value which represents an intermediate result
 * of a [AbstractTokenizer] while constructing a [Token]. The class extends [Token] and adds
 * [status] property to represent the current status or the result of a [Token] creation
 *
 * @property finish is overridden to become variable
 * @property status is the status of the token to be built
 * @property reason keeps a text message describing a reason of a FAILED state, if any
 */
open class TokenBuilder<E: Enum<E>> (
    type: E,
    start: Int,
    finish: Int = start,
    status: BuildingStatus,
    var reason: String = "") : Token<E>(type, start, finish) {

  override var finish: Int = finish
  set(value) {
    require(value >= start) {
      "Illegal attempt to set finish value ($value) less than start ($start)"
    }
    field = value
  }

  var status: BuildingStatus = status
    set(value) {
      require(value != BuildingStatus.NONE) {
        "Illegal attempt to set BuildingStatus ${BuildingStatus.NONE} of a $type TokenBuilder"
      }
      field = value
    }

  /**
   * Builds a [Token]
   * This [TokenBuilder] *MUST* have [BuildingStatus.FINISHED] status before colling this function
   */
  open fun build(): Token<E> {
    check(BuildingStatus.FINISHED == status) {
      "Illegal attempt to build $type token with incorrect status: $status"
    }
    return Token<E>(type, start, finish)
  }


  /**
   * Creates a new [TokenBuilder] with a new type
   *
   * @param newType a type of a new [TokenBuilder]
   * @return a newly created [TokenBuilder]
   */
  fun transformType(newType: E) = TokenBuilder(newType, start, finish, status)
}