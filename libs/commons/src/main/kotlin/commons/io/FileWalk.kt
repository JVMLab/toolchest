package com.jvmlab.commons.io

import java.io.File
import java.nio.file.*


/**
 * The same as [File.walk()] but is tuned to be used for directories. [this] file is excluded and all others
 * are relativilized against [this]
 *
 * @return filtered Sequence<File>
 */
fun File.walkDir (
  direction: FileWalkDirection = FileWalkDirection.TOP_DOWN
): Sequence<File> {
  val parent = this.toPath()
  return this.walk(direction)
             .filterNot { file: File -> Files.isSameFile(parent, file.toPath()) }
             .map { it.relativeTo(this) }
}


/**
 * Applies a [FileMatcher] [fm] to a sequence of files
 * 
 * @return a matching Sequence<File>
 */
fun Sequence<File>.applyFileMatcher (fm: FileMatcher = FileMatcher()): Sequence<File> {
  val matcher = FileSystems.getDefault().getPathMatcher(fm.mPat.getPatternString())

  val fileFilter = { file: File -> 
    matcher.matches(file.toPath()) &&
    when (fm.fType) {
      FileType.REGULAR -> file.isFile
      FileType.DIRECTORY -> file.isDirectory
      FileType.ANY -> true
    }
  }

  return when (fm.mType) {
    MatcherType.INCLUDE -> this.filter(fileFilter)
    MatcherType.EXCLUDE -> this.filterNot(fileFilter)
  }
}


/**
 * Applies a [Array<FileMatcher>] [fma] to a sequence of files obtained by walking starting from [this] file
 * (assumed to be a directory)
 * 
 * @param [fma] Array of file matchers
 * @param [direction] a file walking direction
 * 
 * @return a matching Sequence<File>
 */
fun File.applyFileMatcherArray (
  fma: Array<FileMatcher> = arrayOf(FileMatcher()),
  direction: FileWalkDirection = FileWalkDirection.TOP_DOWN
): Sequence<File> {
  val startSeq: Sequence<File> = this.walkDir(direction)
  if (fma.isEmpty()) {
    return startSeq
  }

  var returnSeq: Sequence<File> = 
    when (fma[0].mType) {
      MatcherType.INCLUDE -> emptySequence()
      MatcherType.EXCLUDE -> startSeq
    }

  for (fm: FileMatcher in fma) {
    when (fm.mType) {
      MatcherType.INCLUDE -> returnSeq += startSeq.applyFileMatcher(fm)
      MatcherType.EXCLUDE -> returnSeq = returnSeq.applyFileMatcher(fm)
    }
  }

  return returnSeq
}


/**
 * Creates a new [File] with the extension to changed to [newExt]
 * 
 * @return newly created [File] instance
 */
fun File.changeExt (newExt: String): File = File(this.nameWithoutExtension + ".$newExt")