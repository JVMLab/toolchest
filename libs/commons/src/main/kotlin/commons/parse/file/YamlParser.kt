package com.jvmlab.commons.parse.file

import java.io.File

import commons.serialization.snakeyaml.Yaml
import com.jvmlab.commons.parse.Parsed
import com.jvmlab.commons.parse.ParsedKey


/**
 * Parses [File] content as YAML
 */
fun File.parseYaml (yaml: Yaml): Parsed<File, Any> =
    Parsed<File, Any>(this, ParsedKey.FILE_CONTENT, yaml::loadMap)


/**
 * Parses source of [Parsed] content as YAML
 */
fun Parsed<File, Any>.parseYaml (yaml: Yaml): Parsed<File, Any> =
    this.mergeResult(ParsedKey.FILE_CONTENT, yaml::loadMap)
