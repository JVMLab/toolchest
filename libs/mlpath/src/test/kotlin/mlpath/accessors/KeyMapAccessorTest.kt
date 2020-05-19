package com.jvmlab.mlpath.accessors

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class KeyMapAccessorTest {

  private val source = sequenceOf(
      mapOf(
          "k1" to 1,
          "k2" to 2
      ),
      mapOf(
          "k1" to 3,
          "k2" to 4
      )
  )

  @Test
  fun `empty source sequence`() {
    val mapAccessor: IMapAccessor = KeyMapAccessor("k3")
    assertTrue(mapAccessor.access(sequenceOf()).none())
  }

  @Test
  fun `non matching key`() {
    val mapAccessor: IMapAccessor = KeyMapAccessor("k3")
    assertTrue(mapAccessor.access(source).none())
  }

  @Test
  fun `matching key`() {
    val mapAccessor: IMapAccessor = KeyMapAccessor("k1")
    val result = mapAccessor.access(source).toList()
    assertEquals(listOf(1, 3), result)
  }
}