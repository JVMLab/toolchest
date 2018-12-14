package com.jvmlab.mlpath.tokens

import com.jvmlab.commons.parse.text.Token
import com.jvmlab.commons.parse.text.TokenStatus


/**
 * Implementation of a generic [Token] class with [TokenType] parameter
 */
class MLToken(
    type: TokenType,
    start: Int,
    finish: Int,
    subTokens: List<Token<TokenType>>
) : Token<TokenType>(type, start, finish, subTokens)