package com.ngh.api.config.security.jwt

import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

object JwtConstants {
    private const val SECRET_KEY_BASE: String = "secret-ngh-important-key-require-too-long" // 우리 서버만 알고 있는 비밀값
	val SECRET_KEY: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY_BASE.toByteArray(Charsets.UTF_8))
	const val EXPIRATION_TIME_MILLIS = 86400000 // 1일
	const val HEADER = "Auth"
}