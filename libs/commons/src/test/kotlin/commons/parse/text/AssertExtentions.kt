package com.jvmlab.commons.parse.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertAll


fun Token<TokenType>.assertEquals(token: Token<TokenType>) {
  assertAll("token",
      { assertEquals(type, token.type, "type mismatch") },
      { assertEquals(start, token.start, "start mismatch") },
      { assertEquals(finish, token.finish, "finish mismatch") }
  )
}