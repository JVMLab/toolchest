package com.jvmlab.mlpath.accessors


class KeyMapAccessor(val key: String) : IMapAccessor {

  override fun access(source: Sequence<Map<String, Any>>): Sequence<Any> =
      source.mapNotNull { it[key] }
}