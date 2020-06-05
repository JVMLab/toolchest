package com.jvmlab.commons.parse.ast

import com.jvmlab.commons.parse.ast.nodes.IBinaryOperator
import com.jvmlab.commons.parse.ast.nodes.INode
import com.jvmlab.commons.parse.lexer.Token


/**
 * Interface to be used by a parser to get an AST node [INode] for a [Token]
 */
interface IGrammar {
  fun <E: Enum<E>> getOperand(token: Token<E>): INode<*>
  fun <E: Enum<E>> getBinaryOperator(token: Token<E>): IBinaryOperator<*, *, *>
}