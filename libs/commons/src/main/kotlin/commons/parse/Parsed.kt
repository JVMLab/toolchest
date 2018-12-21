package com.jvmlab.commons.parse

import java.io.File
import kotlin.reflect.KFunction2


/**
 * Represents a parsed [source] value along with the results of the parsing, which is
 * a [Map<String, Any>] value
 *
 * @property source a value to be parsed
 * @property result a result of the parsing
 * @param key an optional [ParsedKey], if not null then an extra [Map] is created for the [result]
 * with [ParsedKey.key] as a key and parser([source]) as a value
 * @param parser a function to perform the actual parsing
 */
class Parsed<T> (val source: T, key: ParsedKey? = null, parser: (T) -> Map<String, Any>) {

  val result: Map<String, Any> = extraMap(key, parser, source)


  private fun extraMap (key: ParsedKey? = null, parser: (T) -> Map<String, Any>, source: T) =
      key?.let { mapOf(it.key to parser(source)) } ?: parser(source)


  /**
   * Returns a new [Parsed<V>] instance with the original parsing result and the source transformed
   * by the [transform] function
   */
  fun <V> transformSource(transform: (T) -> V): Parsed<V> = Parsed<V>(transform(source)) { result }


  /**
   * Returns a new [Parsed<T>] instance with the original source and the original parsing result
   * merged with the new one produced by the [parser] function
   */
  fun mergeResult(key: ParsedKey? = null, parser: (T) -> Map<String, Any>): Parsed<T> =
      Parsed<T>(source, null) { src: T ->
        val mergedResult = HashMap<String, Any> (result)
        mergedResult.apply {
          this.putAll(extraMap(key, parser, src))
        }
      }
}


/**
 * Holds keys of library parsers. All standard library parsers put their results using these keys
 */
enum class ParsedKey(val key: String) {
  FILE_CONTENT("fileContent"),
  FILE_PATH("filePath"),
  TOKENIZED_STRING("tokenizedString")
}