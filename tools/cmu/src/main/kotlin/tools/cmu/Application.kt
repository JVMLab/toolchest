package com.jvmlab.tools.cmu


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

  parser.parse(args)

  val inputFile = File(input)

  when (val inputExtension = inputFile.extension.lowercase()) {
    "json" -> {

    }
    else -> System.err.println("Unknown input file extension: '$inputExtension'")
  }
}