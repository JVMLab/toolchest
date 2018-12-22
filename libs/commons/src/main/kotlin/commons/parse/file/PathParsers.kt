package com.jvmlab.commons.parse.file

import java.io.File

import com.jvmlab.commons.collections.asList
import com.jvmlab.commons.io.extensionSeparator
import com.jvmlab.commons.parse.Parsed
import com.jvmlab.commons.parse.ParsedKey


/**
 * Parses [File] path and returns a new [Parsed]
 */
fun File.parsePath(): Parsed<File, Any> =
    Parsed<File, Any>(this, ParsedKey.FILE_PATH, ::fileParsePath)


/**
 * Parses path of [File] source of [Parsed] and returns a new [Parsed] with new result merged with
 * previous
 */
fun Parsed<File, Any>.parsePath(): Parsed<File, Any> =
    this.mergeResult(ParsedKey.FILE_PATH, ::fileParsePath)



private fun fileParsePath(file: File): Map<String, Any> = mapOf<String, Any> (
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
