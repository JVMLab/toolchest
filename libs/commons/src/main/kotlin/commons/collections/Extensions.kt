package com.jvmlab.commons.collections


/**
 * Converts an [Iterator<T>] into a [List<T>]
 */
fun <T> Iterator<T>.asList () : List<T> = ArrayList<T>().apply {
  while (hasNext()) {
    this += next()
  }
}