package com.jvmlab.commons.parse



/**
 * Represents a parsed [source] value of type [S] along with the [result] of the parsing,
 * which is a [Map] value with [String] key and values of type [R]
 *
 * @property source a value to be parsed
 * @property result a result of the parsing
 * @param key an optional [ParsedKey], if not null then an extra [Map] is created for the [result]
 * with [ParsedKey.key] as a key and parser([source]) as a value
 * @param parser a function to perform the actual parsing
 */
class Parsed<S, R> {

  val source: S
  val result: Map<String, R>

  /**
   * @param parser a function to perform the actual parsing to the resulting [Map]
   */
  constructor(source: S, parser: (S) -> Map<String, R>) {
    this.source = source
    this.result = parser(source)
  }

  /**
   * @param parsedKey gives a key for the resulting [Map]
   * @param parser a function to perform the actual parsing to the value in the resulting [Map]
   */
  constructor(source: S, parsedKey: ParsedKey, parser: (S) -> R) {
    this.source = source
    this.result = mapOf(parsedKey.key to parser(source))
  }



  /**
   * Returns a new [Parsed] instance with the original parsing result and the source transformed
   * by the [transform] function
   */
  fun <S2> transformSource(transform: (S) -> S2) = Parsed<S2, R>(transform(source)) { result }


  /**
   * Returns a new [Parsed] instance with the original source and the original parsing result
   * merged with the new one produced by the [parser] function
   *
   * @param parser a function to perform the actual parsing to the resulting [Map]
   */
  fun mergeResult(parser: (S) -> Map<String, R>): Parsed<S, R> =
      Parsed<S, R>(source) { src: S ->
        HashMap<String, R>(result).apply {
          this.putAll(parser(src))
        }
      }


  /**
   * Returns a new [Parsed] instance with the original source and the original parsing result
   * merged with the new one produced by the [parser] function
   *
   * @param parsedKey gives a key for the resulting [Map]
   * @param parser a function to perform the actual parsing to the value in the resulting [Map]
   */
  fun mergeResult(parsedKey: ParsedKey, parser: (S) -> R): Parsed<S, R> =
      Parsed<S, R>(source) { src: S ->
        HashMap<String, R>(result).apply {
          this.putAll(mapOf(parsedKey.key to parser(src)))
        }
      }
}


/**
 * Holds keys of library parsers. All standard library parsers put their results using these keys
 */
enum class ParsedKey(val key: String) {
  FILE_CONTENT("fileContent"),
  FILE_PATH("filePath"),
  PARSED_STRING("parsedString")
}