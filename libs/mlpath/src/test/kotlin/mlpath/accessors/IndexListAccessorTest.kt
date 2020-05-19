package com.jvmlab.mlpath.accessors

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.*


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class IndexListAccessorTest {

  private val source = sequenceOf(
      listOf(1, 2),
      listOf(3, 4)
  )

  @Test
  fun `empty source sequence`() {
    val listAccessor: IListAccessor = IndexListAccessor(2)
    assertTrue(listAccessor.access(sequenceOf()).none())
  }

  @Test
  fun `non matching key`() {
    val listAccessor: IListAccessor = IndexListAccessor(4)
    assertTrue(listAccessor.access(source).none())
  }

  @Test
  fun `matching key`() {
    val listAccessor: IListAccessor = IndexListAccessor(1)
    val result = listAccessor.access(source).toList()
    assertEquals(listOf(2, 4), result)
  }
}