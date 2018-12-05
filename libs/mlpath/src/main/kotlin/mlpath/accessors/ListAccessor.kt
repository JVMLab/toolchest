package com.jvmlab.mlpath.accessors


internal class ListAccessor(val index: Int): Accessor<List<Any>> {
  override fun access(source: List<Any>): Any? = source[index]
}