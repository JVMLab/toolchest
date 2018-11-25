package com.jvmlab.sandbox.iotest


import java.io.File

import com.jvmlab.commons.io.*


fun main(args: Array<String>) {
  val file = File("tmp.txt")

  println("Original name: ${file.name}")
  println("With new extn: ${file.changeExt("md").name}")
  println("")

  val pwd = File(".")
  val match = "**/*.*t*"
  val excl = "**/*.{at,len}"
  println("pwd: $pwd")
  println("match: $match")
  println("excl : $excl")
  pwd.walkDir(FileWalkDirection.BOTTOM_UP)
     .applyFileMatcher(FileMatcher(MatchingPattern(match), MatcherType.INCLUDE, FileType.REGULAR))
     .applyFileMatcher(FileMatcher(MatchingPattern(excl), MatcherType.EXCLUDE))
     .forEach{ f: File -> println(f) }

  println("\napplyFileMatcherArray:")
  pwd.applyFileMatcherArray(arrayOf(
     FileMatcher(MatchingPattern(match), MatcherType.INCLUDE, FileType.REGULAR),
     FileMatcher(MatchingPattern(excl), MatcherType.EXCLUDE),
     FileMatcher(MatchingPattern(".*(lookups|kotlin).*", PatternSyntax.REGEX), MatcherType.EXCLUDE)
  )).forEach{ f: File -> println(f) }

  println("\napplyFileMatcherArray: EXCLUDE")
  pwd.applyFileMatcherArray(arrayOf(
     FileMatcher(mType = MatcherType.EXCLUDE, fType = FileType.DIRECTORY),
     FileMatcher(MatchingPattern(excl), MatcherType.EXCLUDE),
     FileMatcher(MatchingPattern(".*(lookups|kotlin).*", PatternSyntax.REGEX), MatcherType.EXCLUDE)
  )).forEach{ f: File -> println(f) }
}