package com.jvmlab.tools.cmu

import java.io.File
import java.io.PrintWriter


fun processJson(inputFile: File, printWriter: PrintWriter) {
  val inputString = inputFile.readText()
  printWriter.print(inputString)
}