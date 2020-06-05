package com.jvmlab.commons.parse.ast.nodes


/**
 * AST node that represents a binary operator and transforms 2 operands: [rightOperand] and [leftOperand].
 */
interface IBinaryOperator<T, L, R> : IOperator<T, R> {
  var leftOperand: INode<L>
  val priority: Int
}