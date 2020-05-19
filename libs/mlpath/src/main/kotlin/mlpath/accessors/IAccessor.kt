package com.jvmlab.mlpath.accessors


interface IAccessor<in T> {
  fun access(source: Sequence<T>): Sequence<Any>
}