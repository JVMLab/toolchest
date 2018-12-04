package com.jvmlab.mlpath.accessors


internal class MapAccessor(val key: String): Accessor<Map<String, Any>> {
  override fun access(source: Map<String, Any>): Any? = source[key]
}