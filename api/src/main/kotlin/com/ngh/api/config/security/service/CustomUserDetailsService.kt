package com.ngh.api.config.security.service

import com.ngh.api.config.security.vo.SessionVo
import com.ngh.core.repository.jpa.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    val userRepository: UserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByLoginId(username) ?: throw UsernameNotFoundException("등록된 회원이 아닙니다.")

        return SessionVo(
            userId = user.id,
            loginId = user.loginId,
            password = user.password,
            role = user.role?.name
        )
    }
}