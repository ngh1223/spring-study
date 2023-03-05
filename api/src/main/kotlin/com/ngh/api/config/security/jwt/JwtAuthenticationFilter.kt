package com.ngh.api.config.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.util.*

class JwtAuthenticationFilter(private val userDetailsService: UserDetailsService):
    AbstractAuthenticationProcessingFilter("/**")  {

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        val jwtToken = request.getHeader(JwtConstants.HEADER)
        if (jwtToken == null) {
            chain.doFilter(request, response)
            return
        }

        try {
            val authentication = attemptAuthentication(request, response)
            successfulAuthentication(request, response, chain, authentication)
        } catch (e: AuthenticationException) {
            unsuccessfulAuthentication(request, response, e)
        }

        chain.doFilter(request, response)
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val jwtToken = request.getHeader(JwtConstants.HEADER)
        val claims: Jws<Claims> = Jwts.parserBuilder()
            .setSigningKey(JwtConstants.SECRET_KEY)
            .build()
            .parseClaimsJws(jwtToken)

        val expired: Boolean = claims.body.expiration.after(Date())

        if (expired) {
            throw object: AuthenticationException("토큰이 만료되었습니다."){}
        }

        val loginId: String? = claims.body.subject

        if (loginId == null) {
            throw UsernameNotFoundException("토큰이 잘못되었습니다.")
        } else {
            val user: UserDetails = userDetailsService.loadUserByUsername(loginId)
            val authToken = UsernamePasswordAuthenticationToken(user.username, user.password)

            return authenticationManager.authenticate(authToken)
        }
    }

}