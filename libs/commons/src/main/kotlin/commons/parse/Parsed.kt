package com.jvmlab.commons.parse

import java.io.File
import kotlin.reflect.KFunction2


/**
 * Represents a parsed [source] value along with the results of the parsing, which is [Map<String, Any>] value
 *
 * @property source a value to be parsed
 * @property result a result of the parsing
 * @param parser a function to perform the actual parsing
 */
class Parsed<T> (val source: T, parser: (T) -> Map<String, Any>) {

  val result: Map<String, Any> = parser(source)


  /**
   * Returns a new [Parsed<V>] instance with the original parsing result and the source transformed
   * by the [transform] function
   */
  fun <V> transformSource(transform: (T) -> V): Parsed<V> = Parsed<V>(transform(this.source)) {this.result}

  /**
   * Returns a new [Parsed<T>] instance with the original source and the original parsing result merged with the new
   * one produced by the [parser] function
   */
  fun mergeResult(parser: (T) -> Map<String, Any>): Parsed<T> = Parsed<T>(this.source) { source: T ->
    val mergedResult = HashMap<String, Any> (this.result)

    mergedResult.apply {
      this.putAll(parser(source))
    }
  }
}