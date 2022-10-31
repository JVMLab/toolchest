package com.jvmlab.tools.cmu

import com.jvmlab.commons.io.filePrintWriterOrOut
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.File


fun main(args: Array<String>) {
  val parser = ArgParser("cmu")

  val input by parser.argument(
    ArgType.String,
    description = "Input file"
  )

  val output by parser.option(
    ArgType.String,
    shortName = "o",
    description = "Output file name"
  )

  val type by parser.option(
    ArgType.Choice(listOf("json", "conf"), { it }),
    shortName = "t",
    description = "Explicit input file type"
  )

  parser.parse(args)

  val inputFile = File(input)
  val printWriter = filePrintWriterOrOut(output)
  val outputName = output ?: "STDOUT"

  when (val inputType = (type ?: inputFile.extension).lowercase()) {
    "json" -> {
      println("Processing JSON: from '$input' to '$outputName'")
      processJson(inputFile, printWriter)
      printWriter.close()
    }
    else -> System.err.println(
      "Unknown input file extension: '$inputType'\n" +
          "Try to specify input file type explicitly using -t option"
    )
  }
}