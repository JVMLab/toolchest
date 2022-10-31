package com.jvmlab.tools.cmu

import java.io.File
import java.io.PrintWriter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString


private val parser = Json {
  prettyPrint = true
  isLenient = true
}

fun processJson(inputFile: File, printWriter: PrintWriter) {
  val jsonElement = parser.decodeFromString<JsonElement>(inputFile.readText())
  printWriter.print(parser.encodeToString(jsonElement))
}