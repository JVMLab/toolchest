package com.jvmlab.sandbox.parsetest

import java.io.File

import commons.serialization.snakeyaml.Yaml
import com.jvmlab.commons.parse.ParsedKey
import com.jvmlab.commons.parse.file.parsePath
import com.jvmlab.commons.parse.file.parseYaml
import com.jvmlab.commons.parse.text.*
import com.jvmlab.commons.parse.text.tokenizers.*


fun main(args: Array<String>) {

  val file = File("src/test/map.yaml")
  val yaml = Yaml()
  val parsedFile = file.parseYaml(yaml)

  println("source: ${parsedFile.source}")
  println("result: ${parsedFile.result}\n")

  val parsedPath = file.parseYaml(yaml).parsePath()

  println("source: ${parsedPath.source}")
  println("result: ${parsedPath.result}\n")


  val wordStrList = listOf(
      "   ",
      "xxx",
      "aaa ",
      " ttt",
      "  ttt  kkk 123  x ",
      "xxx  x   "
  )

  println("\n*********** WordTokenizer positive")
  wordStrList.forEach { str: String ->
    val parsedString = str.tokenize(WordTokenizer(TokenType.WORD), TokenType.WSPC)
    println("\nsrc  : '${parsedString.source}'")
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      it.prettyPrint(parsedString.source)
    }
    println(parsedString.result)
  }


  val separatorStrList = listOf(
      "x,y",
      "xxx,ttt  ",
      "xxx, ttt,x",
      ",xxx,",
      ",,xxx,  , tt ,,"
  )

  println("\n\n*********** SingleCharTokenizer positive")
  separatorStrList.forEach { str: String ->
    val parsedString = str.tokenize(SingleCharTokenizer(TokenType.SPRT, ','), TokenType.WORD)
    println("\nsrc  : '${parsedString.source}'")
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      it.prettyPrint(parsedString.source)
    }
    println(parsedString.result)
  }


  println("\n\n*********** MultiTokenizer negative")
  wordStrList.forEach { str: String ->
    try {
      val parsedString = str.tokenize(
          AlternativeTokenizer(
              TokenType.DFLT,
              listOf(
                  WordTokenizer(TokenType.WORD),
                  WordTokenizer(TokenType.WORD)
              )
          ),
          TokenType.WSPC)


      println("\nsrc  : '${parsedString.source}'")
      parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
        it.prettyPrint(parsedString.source)
      }
    } catch (e: Exception) {
      println("\nsrc  : '$str'")
      println(e.message)
    }
  }


  println("\n\n*********** MultiTokenizer positive")
  separatorStrList.forEach { str: String ->
    val parsedString = str.tokenize(
        AlternativeTokenizer(
            TokenType.DFLT,
            listOf(
                WordTokenizer(TokenType.WORD),
                SingleCharTokenizer(TokenType.SPRT, ',')
            )
        ),
        TokenType.WSPC)
    println("\nsrc  : '${parsedString.source}'")
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      it.prettyPrint(parsedString.source)
    }
  }


  val bracketsStrList = listOf(
      "[xxx]",
      "[xxx] [ttt,xx] ",
      " [tt,xxx,t]"
  )

  println("\n\n*********** BracketsTokenizer positive")
  bracketsStrList.forEach { str: String ->
    println("\nsrc  : '$str'")
    val parsedString = str.tokenize(
        BracketsTokenizer(
            TokenType.BRCK,
            SubTokenizer(
                AlternativeTokenizer(
                    TokenType.DFLT,
                    listOf(
                        WordTokenizer(TokenType.WORD),
                        SingleCharTokenizer(TokenType.SPRT, ',')
                    )
                )
            ),TokenType.L_BR, TokenType.R_BR
        ),
        TokenType.WSPC)
    parsedString.result[ParsedKey.PARSED_STRING.key]?.forEach {
      it.prettyPrint(parsedString.source)
      if (it is ComplexToken) {
        println("${it.type} has ${it.subTokens.size} sub-tokens")
      }
    }
  }
}


enum class TokenType {
  DFLT, WORD, WSPC, SPRT, BRCK, L_BR, R_BR
}