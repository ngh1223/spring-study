package com.ngh.core.domain

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.time.LocalDateTime

@Entity
@Table(name = "user")
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    val id: Long = 0

    @Column(name = "login_id")
    var loginId: String? = null

    @Column(name = "password")
    var password: String? = null

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var role: Role? = null

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    var createdDate: LocalDateTime = LocalDateTime.now()
        private set
}

enum class Role {
    ROLE_USER, ROLE_ADMIN
}