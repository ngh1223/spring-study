package com.ngh.api.config.security.vo

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class SessionVo(
    userId: Long,
    loginId: String?,
    password: String?,
    role: String?
): UserDetails {
    var userId: Long = 0
    var loginId: String? = null
    private var password: String? = null
    private var role: String? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities: MutableCollection<GrantedAuthority> = mutableListOf()
        authorities.add(SimpleGrantedAuthority(role))

        return authorities
    }

    override fun getPassword(): String? {
        return this.password
    }

    override fun getUsername(): String? {
        return this.loginId
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}