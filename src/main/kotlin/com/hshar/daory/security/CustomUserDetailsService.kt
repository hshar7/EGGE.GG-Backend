package com.hshar.daory.security

import com.hshar.daory.exception.ResourceNotFoundException
import com.hshar.daory.repository.UserRepository
import findOne
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
        val user = userRepository.findByUsername(username)
                ?: throw ResourceNotFoundException("User", "username", username)
        return UserPrincipal.create(user)
    }

    @Transactional
    fun loadUserById(id: String): UserDetails {
        val user = userRepository.findOne(id) ?: throw UsernameNotFoundException("User not found with id $id")
        return UserPrincipal.create(user)
    }
}
