package com.jvmlab.mlpath.resolver

/**
 * [IResolver] resolves a path in an ML-object
 *
 * [resolve] functions should return
 *  - a list of elements from if a given path was resolved into a list of ML-objects
 *  - a single element from that sequence if the path was resolved into a single element
 *  - null in case the path doesn't match the source ML-object (map or list)
 */
interface IResolver {
  fun resolve(map: Map<String, Any>): Any?
  fun resolve(list: List<Any>): Any?
}