package com.jvmlab.commons.parse.ast

import com.jvmlab.commons.parse.ast.nodes.IBinaryOperator
import com.jvmlab.commons.parse.ast.nodes.INode
import com.jvmlab.commons.parse.lexer.Token


/**
 * The very basic (although sufficient for simple cases) [IGrammar] implementation
 */
class BasicGrammar<E: Enum<E>>(
    private val operands: Map<E, INode<*>>,
    private val binaryOperators: Map<E, IBinaryOperator<*, *, *>>
) : IGrammar<E> {

  override fun getOperand(token: Token<E>): INode<*>? = operands[token.type]

  override fun getBinaryOperator(token: Token<E>): IBinaryOperator<*, *, *>? = binaryOperators[token.type]
}