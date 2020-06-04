package com.jvmlab.commons.parse.ast.nodes


/**
 * AST node that represents an unary operator and transforms [rightOperand]. It acts upon type [R]
 * and returns type [T], although both types may be the same
 */
interface IOperator<T, R> : INode<T> {
  var rightOperand: INode<R>
}