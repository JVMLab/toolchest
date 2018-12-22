package com.jvmlab.sandbox.parsetest

import java.io.File

import com.jvmlab.commons.io.*
import com.jvmlab.commons.parse.ParsedKey
import com.jvmlab.commons.parse.file.parsePath
import com.jvmlab.commons.parse.file.parseYaml
import com.jvmlab.commons.parse.text.Token
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


  val str = "  ttt  kkk 123  x"
  val parsedString = str.parse(WordTokenizer<TokenType>(TokenType.WORD), TokenType.WHITESPACE)
  println("source: '${parsedString.source}'")
  (parsedString.result[ParsedKey.PARSED_STRING.key] as List<Token<TokenType>>).forEach {
    token ->
    println("${token.type} = '${token.asString(parsedString.source)}'")
  }

}


enum class TokenType {
  WORD, WHITESPACE
}