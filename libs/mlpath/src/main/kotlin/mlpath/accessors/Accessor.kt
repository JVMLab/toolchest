package com.jvmlab.mlpath.accessors


internal interface Accessor<in T> {
  fun access(source: T): Any?
}