package com.jvmlab.commons.parse.file

import java.io.File

import com.jvmlab.commons.collections.asList
import com.jvmlab.commons.io.extensionSeparator
import com.jvmlab.commons.parse.Parsed


/**
 * Parses [File] path and returns a new [Parsed<File>]
 *
 * @param key an optional key to store the parsing result
 */
fun File.parsePath (key: String = FILE_PATH): Parsed<File> = Parsed<File>(this) { f: File ->
  fileParsePath(f, key)
}


/**
 * Parses source of [Parsed<File>] path and returns a new [Parsed<File>]
 *
 * @param key an optional key to store the parsing result
 */
fun Parsed<File>.parsePath (key: String = FILE_PATH): Parsed<File> = this.mergeResult { f: File ->
  fileParsePath(f, key)
}



private const val FILE_PATH = "filePath"


private fun fileParsePath(file: File, key: String): Map<String, Any> = mapOf (
  key to mapOf (
      "separator"            to File.separator,
      "pathSeparator"        to File.pathSeparator,
      "extensionSeparator"   to file.extensionSeparator,
      "parent"               to (file.parent ?: ""),
      "pathList"             to file.toPath().iterator().asList(),
      "name"                 to file.name,
      "extension"            to file.extension,
      "nameWithoutExtension" to file.nameWithoutExtension,
      "nameList"             to file.name.split(file.extensionSeparator)
    )
  )
