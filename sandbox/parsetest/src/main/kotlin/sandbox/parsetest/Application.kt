package com.jvmlab.sandbox.parsetest

import java.io.File

import com.jvmlab.commons.io.Yaml
import com.jvmlab.commons.parse.ParsedKey
import com.jvmlab.commons.parse.file.parsePath
import com.jvmlab.commons.parse.file.parseYaml
import com.jvmlab.commons.parse.text.WordTokenizer
import com.jvmlab.commons.parse.text.parse


fun main(args: Array<String>) {

  val file = File("src/test/map.yaml")
  val yaml = Yaml()
  val parsedFile = file.parseYaml(yaml)

  println("source: ${parsedFile.source}")
  println("result: ${parsedFile.result}\n")

  val parsedPath = file.parseYaml(yaml).parsePath()

  println("source: ${parsedPath.source}")
  println("result: ${parsedPath.result}\n")


  val strList = listOf<String>(
      "xxx",
      " ttt",
      "  ttt  kkk 123  x",
      "xxx  x   "
  )

  strList.forEach { str: String ->
    val parsedString = str.parse(WordTokenizer<TokenType>(TokenType.WORD), TokenType.WSPC)
    println("\nsrc  : '${parsedString.source}'")
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      token ->
      val indent = " ".repeat(token.start)
      println("${token.type} : $indent'${token.asString(parsedString.source)}'")
    }
  }

}


enum class TokenType {
  WORD, WSPC
}