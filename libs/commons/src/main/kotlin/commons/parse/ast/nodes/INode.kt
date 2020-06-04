package com.jvmlab.commons.parse.ast.nodes


/**
 * Basic operand AST node that returns [T] upon execution
 */
interface INode<T> {
  fun execute(): T
}