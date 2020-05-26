package com.jvmlab.mlpath.resolver

import com.jvmlab.mlpath.accessors.IAccessor
import com.jvmlab.mlpath.accessors.IndexListAccessor
import com.jvmlab.mlpath.accessors.KeyMapAccessor

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.api.Assertions.*
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ResolverTest {

  private val input =
      mapOf<String, Any>(
          "person" to mapOf(
              "firstName" to "John",
              "lastName" to "Doe",
              "kids" to listOf(
                  mapOf(
                      "firstName" to "James",
                      "lastName" to "Doe"
                  ),
                  mapOf(
                      "firstName" to "Judy",
                      "lastName" to "Doe"
                  )
              )
          )
      )

  fun testArgs(): Stream<Arguments> =
      Stream.of(
          Arguments.of(
              "/person",
              listOf(KeyMapAccessor("person")),
              input["person"]
          ),
          Arguments.of(
              "/person/firstName",
              listOf(
                  KeyMapAccessor("person"),
                  KeyMapAccessor("firstName")
              ),
              @Suppress("UNCHECKED_CAST")
              (input["person"] as Map<String, Any>)["firstName"]
          ),
          Arguments.of(
              "/person/kids[1]",
              listOf(
                  KeyMapAccessor("person"),
                  KeyMapAccessor("kids"),
                  IndexListAccessor(1)
              ),
              @Suppress("UNCHECKED_CAST")
              (input["person"] as Map<String, List<Any>>)["kids"]?.get(1)
          )
      )


  @ParameterizedTest(name = "{0}")
  @MethodSource("testArgs")
  fun resolver(pathString: String, path: List<IAccessor<*>>, expected: Any) {
    val resolver = Resolver(path)
    val result = resolver.resolve(input)
    assertEquals(expected, result)
  }
}