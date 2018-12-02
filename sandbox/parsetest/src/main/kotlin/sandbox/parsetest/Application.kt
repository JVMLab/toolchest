package com.jvmlab.sandbox.parsetest

import java.io.File

import com.jvmlab.commons.io.*
import com.jvmlab.commons.parse.Parsed


fun main(args: Array<String>) {

  val file = File("src/test/map.yaml")
  val yaml = Yaml()
  val parsedFile = Parsed<File>(file, yaml::loadMap)

  println("source: ${parsedFile.source}")
  println("result: ${parsedFile.result}")
}