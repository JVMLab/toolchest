package com.jvmlab.commons.parse.text


/**
 * Provides a read-only wrapper class around [TokenBuilder]
 */
inline class RTokenBuilder<E: Enum<E>>(private val instance: TokenBuilder<E>) {
  // Wrapper fields for read-only instance access
  val type: E get() = instance.type
  val start: Int get() = instance.start
  val finish: Int get() = instance.finish
  val status: BuildingStatus get() = instance.status


  fun build(): Token<E> = instance.build()


  /**
   * Duplicates [instance] and provides an option to change some properties if required
   */
  fun duplicate(
      type: E = instance.type,
      start: Int = instance.start,
      finish: Int = instance.finish,
      status: BuildingStatus = instance.status): TokenBuilder<E> =
      TokenBuilder(type, start, finish, status)
}