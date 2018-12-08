package com.jvmlab.commons.parse.file

import java.io.File

import com.jvmlab.commons.collections.asList
import com.jvmlab.commons.io.Yaml
import com.jvmlab.commons.io.extensionSeparator
import com.jvmlab.commons.parse.Parsed



/**
 * Parses [File] content as YAML
 *
 * @param key an optional key to store the parsing result
 */
fun File.parseYaml (yaml: Yaml, key: String = FILE_CONTENT): Parsed<File> = Parsed<File>(this) { f: File ->
  mapOf(key to yaml.loadMap(f))
}


/**
 * Parses source of [Parsed<File>] content as YAML
 *
 * @param key an optional key to store the parsing result
 */
fun Parsed<File>.parseYaml (yaml: Yaml, key: String = FILE_CONTENT): Parsed<File> = this.mergeResult { f: File ->
  mapOf (key to yaml.loadMap(f))
}



private const val FILE_CONTENT = "fileContent"