package com.jvmlab.tools.cmu


import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import java.io.File
import java.io.PrintWriter


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
  val printWriter = output?.let {
    PrintWriter(it)
  } ?: run {
    PrintWriter(System.out)
  }

  when (val inputExtension = inputFile.extension.lowercase()) {
    "json" -> {
      println("Processing JSON $input")
      processJson(inputFile, printWriter)
      printWriter.close()
    }
    else -> System.err.println("Unknown input file extension: '$inputExtension'")
  }
}