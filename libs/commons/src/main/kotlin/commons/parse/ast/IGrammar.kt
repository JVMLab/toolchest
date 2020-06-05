package com.jvmlab.commons.parse.ast

import com.jvmlab.commons.parse.ast.nodes.IBinaryOperator
import com.jvmlab.commons.parse.ast.nodes.INode
import com.jvmlab.commons.parse.lexer.Token


/**
 * Interface to be used by a parser to get an AST node [INode] for a [Token]. If no node is found
 * for a token then null is returned
 */
interface IGrammar<E: Enum<E>> {
  fun getOperand(token: Token<E>): INode<*>?
  fun getBinaryOperator(token: Token<E>): IBinaryOperator<*, *, *>?
}