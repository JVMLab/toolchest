package com.jvmlab.commons.parse

import java.io.File

import com.jvmlab.commons.collections.asList
import com.jvmlab.commons.io.*


/**
 * Parses [File] path and returns a new [Parsed<File>]
 */
fun File.parsePath (key: String = filePath): Parsed<File> = Parsed<File>(this) { f: File ->
  fileParsePath(f, key)
}


/**
 * Parses source of [Parsed<File>] path and returns a new [Parsed<File>]
 */
fun Parsed<File>.parsePath (key: String = filePath): Parsed<File> = this.mergeResult{ f: File ->
  fileParsePath(f, key)
}


/**
 * Parses [File] content as YAML
 */
fun File.parseYaml (yaml: Yaml, key: String = fileContent): Parsed<File> = Parsed<File>(this) { f: File ->
  mapOf (key to yaml.loadMap(f))
}



private const val filePath = "filePath"
private const val fileContent = "fileContent"


private fun fileParsePath(file: File, key: String): Map<String, Any> = mapOf (
  key to mapOf (
      "separator"            to File.separator,
      "pathSeparator"        to File.pathSeparator,
      "extensionSeparator"   to file.extensionSeparator,
      "name"                 to file.name,
      "extension"            to file.extension,
      "nameWithoutExtension" to file.nameWithoutExtension,
      "parent"               to (file.parent ?: ""),
      "pathList"             to file.toPath().iterator().asList()
    )
  )
