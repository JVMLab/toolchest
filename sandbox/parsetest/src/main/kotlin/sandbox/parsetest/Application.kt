package com.jvmlab.sandbox.parsetest

import java.io.File

import com.jvmlab.commons.io.Yaml
import com.jvmlab.commons.parse.ParsedKey
import com.jvmlab.commons.parse.file.parsePath
import com.jvmlab.commons.parse.file.parseYaml
import com.jvmlab.commons.parse.text.AbstractTokenizer
import com.jvmlab.commons.parse.text.BuildingDetails
import com.jvmlab.commons.parse.text.BuildingStatus
import com.jvmlab.commons.parse.text.MultiTokenizer
import com.jvmlab.commons.parse.text.RTokenBuilder
import com.jvmlab.commons.parse.text.SingleCharTokenizer
import com.jvmlab.commons.parse.text.TokenBuilder
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


  val wordStrList = listOf<String>(
      "   ",
      "xxx",
      "aaa ",
      " ttt",
      "  ttt  kkk 123  x ",
      "xxx  x   "
  )

  println("\n*********** WordTokenizer positive")
  wordStrList.forEach { str: String ->
    val parsedString = str.parse(WordTokenizer<TokenType>(TokenType.WORD), TokenType.WSPC)
    println("\nsrc  : '${parsedString.source}'")
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      token ->
      val indent = " ".repeat(token.start)
      println("${token.type} : $indent'${token.asString(parsedString.source)}'")
    }
  }


  val separatorStrList = listOf<String>(
      "xxx,ttt  ",
      "xxx, ttt,x",
      ",xxx,",
      ",,xxx,  , tt ,,"
  )

  println("\n\n*********** SingleCharTokenizer positive")
  separatorStrList.forEach { str: String ->
    val parsedString = str.parse(SingleCharTokenizer(TokenType.SPRT, ','), TokenType.WORD)
    println("\nsrc  : '${parsedString.source}'")
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      token ->
      val indent = " ".repeat(token.start)
      println("${token.type} : $indent'${token.asString(parsedString.source)}'")
    }
  }


  println("\n\n*********** MultiTokenizer negative")
  wordStrList.forEach { str: String ->
    try {
      val parsedString = str.parse(
          MultiTokenizer(
              TokenType.DFLT,
              listOf<AbstractTokenizer<TokenType>>(
                  WordTokenizer<TokenType>(TokenType.WORD),
                  WordTokenizer<TokenType>(TokenType.WORD)
              )),
          TokenType.WSPC)


      println("\nsrc  : '${parsedString.source}'")
      parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach { token ->
        val indent = " ".repeat(token.start)
        println("${token.type} : $indent'${token.asString(parsedString.source)}'")
      }
    } catch (e: Exception) {
      println("\nsrc  : '$str'")
      println(e.message)
    }
  }


  println("\n\n*********** MultiTokenizer positive")
  separatorStrList.forEach { str: String ->
    val parsedString = str.parse(
        MultiTokenizer(
            TokenType.DFLT,
            listOf<AbstractTokenizer<TokenType>>(
                WordTokenizer<TokenType>(TokenType.WORD),
                SingleCharTokenizer(TokenType.SPRT, ',')
            )),
        TokenType.WSPC)
    println("\nsrc  : '${parsedString.source}'")
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      token ->
      val indent = " ".repeat(token.start)
      println("${token.type} : $indent'${token.asString(parsedString.source)}'")
    }
  }

}


enum class TokenType {
  DFLT, WORD, WSPC, SPRT
}