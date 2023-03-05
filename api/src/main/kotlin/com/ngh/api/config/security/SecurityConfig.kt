package com.ngh.api.config.security

import com.ngh.api.config.security.jwt.JwtAuthenticationFilter
import com.ngh.api.config.security.jwt.JwtLoginAuthenticationFilter
import com.ngh.api.config.security.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    val userDetailsService: CustomUserDetailsService,
    val authenticationConfiguration: AuthenticationConfiguration
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .addFilterBefore(jwtLoginAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    fun jwtLoginAuthenticationFilter(): JwtLoginAuthenticationFilter {
        val jwtLoginAuthenticationFilter = JwtLoginAuthenticationFilter()
        jwtLoginAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration))

        return jwtLoginAuthenticationFilter
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        val jwtAuthenticationFilter = JwtAuthenticationFilter(userDetailsService)
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(authenticationConfiguration))

        return jwtAuthenticationFilter
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun roleHierarchy(): RoleHierarchyImpl {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER > ROLE_GUEST")
        return roleHierarchy
    }

    @Bean
    fun expressionHandler(): DefaultWebSecurityExpressionHandler {
        val expressionHandler = DefaultWebSecurityExpressionHandler()
        expressionHandler.setRoleHierarchy(roleHierarchy())
        return expressionHandler
    }

}