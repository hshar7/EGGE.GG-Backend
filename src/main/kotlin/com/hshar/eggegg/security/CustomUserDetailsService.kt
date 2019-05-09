package com.hshar.eggegg.security

import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByPublicAddress(username)
                .orElseThrow { ResourceNotFoundException("User", "publicAddress", username) }
        return UserPrincipal.create(user)
    }

    @Transactional
    fun loadUserById(id: String): UserDetails {
        val user = userRepository.findById(id)
            .orElseThrow { UsernameNotFoundException("User not found with id $id") }
        return UserPrincipal.create(user)
    }
}
