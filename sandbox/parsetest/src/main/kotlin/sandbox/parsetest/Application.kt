package com.jvmlab.sandbox.parsetest

import java.io.File

import com.jvmlab.commons.io.*
import com.jvmlab.commons.parse.*


fun main(args: Array<String>) {

  val file = File("src/test/map.yaml")
  val yaml = Yaml()
  val parsedFile = file.parseYaml(yaml)

  println("source: ${parsedFile.source}")
  println("result: ${parsedFile.result}")
}