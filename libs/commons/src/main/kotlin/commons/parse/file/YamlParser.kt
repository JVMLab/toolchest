package com.jvmlab.commons.parse.file

import java.io.File

import com.jvmlab.commons.io.Yaml
import com.jvmlab.commons.parse.Parsed
import com.jvmlab.commons.parse.ParsedKey


/**
 * Parses [File] content as YAML
 *
 * @param key an optional key to store the parsing result
 */
fun File.parseYaml (yaml: Yaml): Parsed<File> =
    Parsed<File>(this, ParsedKey.FILE_CONTENT, yaml::loadMap)


/**
 * Parses source of [Parsed<File>] content as YAML
 *
 * @param key an optional key to store the parsing result
 */
fun Parsed<File>.parseYaml (yaml: Yaml): Parsed<File> =
    this.mergeResult(ParsedKey.FILE_CONTENT, yaml::loadMap)
