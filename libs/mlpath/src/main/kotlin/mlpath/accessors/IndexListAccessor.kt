package com.jvmlab.mlpath.accessors


class IndexListAccessor(val index: Int) : IListAccessor {

  override fun access(source: Sequence<List<Any>>): Sequence<Any> =
      source.mapNotNull { it.getOrNull(index) }
}