package com.jvmlab.commons.io


/**
 * An enumeration to describe possible syntaxes of patterns used for files matching
 */
enum class PatternSyntax(val value: String) {
  GLOB("glob"),
  REGEX("regex")
}


/**
 * An enumeration to describe possible file types to match
 */
enum class FileType {
  ANY,
  DIRECTORY,
  REGULAR
}


/**
 * An enumeration to describe possible pattern matching types
 */
enum class MatcherType {
  INCLUDE,
  EXCLUDE
}


/**
 * Incapsulates matching pattern details and provides a default pattern to match any file
 */
class MatchingPattern (
  val pattern: String = "**",
  val syntax: PatternSyntax = PatternSyntax.GLOB
) {
  
  /**
   * Returns a syntax and pattern String required by [java.nio.file.FileSystem.getPathMatcher()]
   */
  fun getPatternString(): String { return "${syntax.value}:$pattern" }

}

/**
 * Describes a full file matcher and provides default values to match any file
 */
data class FileMatcher (
  val mPat: MatchingPattern = MatchingPattern(),
  val mType: MatcherType = MatcherType.INCLUDE,
  val fType: FileType = FileType.ANY
)