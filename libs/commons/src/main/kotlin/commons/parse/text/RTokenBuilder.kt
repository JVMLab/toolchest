package com.jvmlab.commons.parse.text


/**
 * Provides a read-only wrapper class around [TokenBuilder]
 */
inline class RTokenBuilder<E: Enum<E>>(private val instance: TokenBuilder<E>) {
  // Wrapper fields for read-only instance access
  val type: E get() = instance.type
  val start: Int get() = instance.start
  val finish: Int get() = instance.finish
  val details: BuildingDetails get() = instance.details


  fun build(): Token<E> = instance.build()


  /**
   * Duplicates [instance] providing an option to change some properties if required
   */
  fun duplicate(
      type: E = instance.type,
      start: Int = instance.start,
      finish: Int = instance.finish,
      details: BuildingDetails = instance.details): TokenBuilder<E> =
      TokenBuilder(type, start, finish, details)
}