package com.ngh.api.config.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.ngh.api.config.security.dto.LoginDto
import com.ngh.api.config.security.vo.SessionVo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.util.*


class JwtLoginAuthenticationFilter:
    AbstractAuthenticationProcessingFilter("/user/login") {

    // 로그인 시도
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication {
        val objectMapper = ObjectMapper()
        val loginDto: LoginDto = objectMapper.readValue(request.inputStream, LoginDto::class.java)

        val authenticationToken = UsernamePasswordAuthenticationToken(
            loginDto.loginId,
            loginDto.password
        )

        return authenticationManager.authenticate(authenticationToken)
    }

    // 인증 성공
    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authentication: Authentication
    ) {
        val sessionVo: SessionVo = authentication.principal as SessionVo
        val tokenClaims: Claims = Jwts.claims().setSubject(sessionVo.loginId)
        val now = Date()

        val jwtToken: String = Jwts.builder()
            .signWith(JwtConstants.SECRET_KEY)
            .setClaims(tokenClaims)
            .setExpiration(Date(now.time + JwtConstants.EXPIRATION_TIME_MILLIS))
            .setIssuedAt(now)
            .signWith(JwtConstants.SECRET_KEY, SignatureAlgorithm.HS256)
            .compact()

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write("""{"jwtToken": $jwtToken}""")
    }
}