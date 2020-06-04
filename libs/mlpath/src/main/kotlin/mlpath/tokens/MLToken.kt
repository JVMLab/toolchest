package com.jvmlab.mlpath.tokens

import com.jvmlab.commons.parse.lexer.Token
import com.jvmlab.commons.parse.lexer.ComplexToken


/**
 * Implementation of a generic [ComplexToken] class with [TokenType] parameter
 */
class MLToken(
    type: TokenType,
    start: Int,
    finish: Int,
    subTokens: List<Token<TokenType>>
) : ComplexToken<TokenType>(type, start, finish, subTokens)