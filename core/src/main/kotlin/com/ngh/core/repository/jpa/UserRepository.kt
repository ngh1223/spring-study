package com.ngh.core.repository.jpa

import com.ngh.core.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun findByLoginId(loginId: String?): User?
}