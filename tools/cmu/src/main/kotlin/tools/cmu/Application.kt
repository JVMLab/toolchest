package com.jvmlab.tools.cmu


import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType


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
}