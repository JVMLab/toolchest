package com.jvmlab.commons.parse.ast.nodes

import com.jvmlab.commons.parse.lexer.ComplexToken


/**
 * AST node to be used for functions of any number of arguments. It's produced from a [ComplexToken],
 * but in contrast to [IComplexNode] it has its own [parse] method to parse sub-tokens of the [ComplexToken].
 */
interface IFunction<T> : INode<T> {
  fun  <E: Enum<E>> parse(complexToken: ComplexToken<E>)
}