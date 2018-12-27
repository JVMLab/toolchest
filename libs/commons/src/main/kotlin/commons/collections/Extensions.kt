package com.jvmlab.commons.collections


/**
 * Converts an [Iterator] into a [List]
 */
fun <T> Iterator<T>.asList(): List<T> = ArrayList<T>().apply {
  while (hasNext()) {
    this.add(next())
  }
}