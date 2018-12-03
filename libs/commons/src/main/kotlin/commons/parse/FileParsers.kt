package com.jvmlab.commons.parse

import java.io.File
import java.nio.file.Path

import com.jvmlab.commons.collections.asList
import com.jvmlab.commons.io.Yaml


/**
 * Parses [File] path and returns [Parsed<File>]
 */
fun File.parsePath (): Parsed<File> = Parsed<File>(this, ::fileParsePath)


/**
 * Parses [File] content as YAML
 */
fun File.parseYaml (yaml: Yaml): Parsed<File> = Parsed<File>(this, yaml::loadMap)



private fun fileParsePath(file: File): Map<String, Any> {
  val path: Path = file.toPath()

  return mapOf(
      "separator" to File.separator,
      "pathSeparator" to File.pathSeparator,
      "pathList" to path.iterator().asList()
  )
}