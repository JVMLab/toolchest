package com.jvmlab.mlpath.resolver

import com.jvmlab.mlpath.accessors.IAccessor
import com.jvmlab.mlpath.accessors.IListAccessor
import com.jvmlab.mlpath.accessors.IMapAccessor


/**
 * [Resolver] implements [IResolver]. It keeps a list of [IAccessor] as a path to resolve
 *
 * @property path it resolves
 */
class Resolver(private val path: List<IAccessor<*>>) : IResolver {

  override fun resolve(map: Map<String, Any>): Any? {
    if (path.isEmpty())
      return map

    if (path.first() !is IMapAccessor)
      return null

    return resolveSource(sequenceOf(map))
  }

  override fun resolve(list: List<Any>): Any? {
    if (path.isEmpty())
      return list

    if (path.first() !is IListAccessor)
      return null

    return resolveSource(sequenceOf(list))
  }


  private fun resolveSource(source: Sequence<Any>): Any? {
    var result = source

    for(accessor in path) {
      when (accessor) {
        is IListAccessor ->
          @Suppress("UNCHECKED_CAST")
          result = accessor.access((result.filter { it is List<*> }) as Sequence<List<Any>>)
        is IMapAccessor ->
          @Suppress("UNCHECKED_CAST")
          result = accessor.access((result.filter { it is Map<*,*> }) as Sequence<Map<String, Any>>)
      }
    }

    val list = result.toList()
    if (list.size > 1)
      return list
    if (list.size == 1)
      return list.first()

    return null
  }
}