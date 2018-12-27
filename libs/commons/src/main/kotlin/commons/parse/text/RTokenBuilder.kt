package com.jvmlab.commons.parse.text


/**
 * Provides read-only wrapper class around [TokenBuilder]
 */
class RTokenBuilder<E: Enum<E>>(private val instance: TokenBuilder<E>) {
  fun type(): E = instance.type
  fun start(): Int = instance.start
  fun finish(): Int = instance.finish
  fun status(): BuildingStatus = instance.status
}