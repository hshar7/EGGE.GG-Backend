package com.hshar.eggegg.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.hshar.eggegg.model.permanent.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.ArrayList
import java.util.Objects

class UserPrincipal private constructor(
        private val id: String,
        private val username: String,
        val name: String,
        @field:JsonIgnore
        private val email: String,
        @field:JsonIgnore
        private val password: String,
        private val authorities: Collection<GrantedAuthority>) : UserDetails {

    fun getId(): String {
        return id
    }

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

    fun getEmail(): String {
        return email
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

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UserPrincipal?
        return id == that!!.id
    }

    override fun hashCode(): Int {

        return Objects.hash(id)
    }

    companion object {
        fun create(user: User): UserPrincipal {
            return UserPrincipal(
                    user.id,
                    user.username,
                    user.name,
                    user.email,
                    user.password,
                    ArrayList()
            )
        }
    }
}
