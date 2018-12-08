package com.jvmlab.mlpath.tokens

import com.jvmlab.commons.parse.text.Token


class MLToken(
    source: CharSequence,
    start: Int,
    stop: Int,
    type: Types): Token<Types>(source, start, stop, type) {
}