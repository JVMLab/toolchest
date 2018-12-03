package com.jvmlab.commons.io

import java.io.File


/**
 * Read-only extension property representing a character to separate file extensions
 */
val File.extensionSeparatorChar: Char
    get() = '.'


/**
 * Read-only extension property representing a one-character [String] to separate file extensions
 */
val File.extensionSeparator: String
  get() = this.extensionSeparatorChar.toString()


/**
 * Creates a new [File] with the extension to changed to [newExt]
 * 
 * @return newly created [File] instance
 */
fun File.changeExt (newExt: String): File = File(this.nameWithoutExtension + ".$newExt")