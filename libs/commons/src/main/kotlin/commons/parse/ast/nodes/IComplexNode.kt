package com.jvmlab.commons.parse.ast.nodes


/**
 * AST node to be used for expressions in parenthesis. [IComplexNode] is produced from a [ComplexToken], and
 * sub-tokens of the complex token are parsed by the same external parser which produces [IComplexNode].
 */
interface IComplexNode<T> : INode<T> {
  val childNode: INode<T>
  override fun execute(): T = childNode.execute()
}