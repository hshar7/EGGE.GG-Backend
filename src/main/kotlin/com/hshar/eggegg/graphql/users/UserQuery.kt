package com.hshar.eggegg.graphql.users

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.model.transient.payload.JwtAuthenticationResponse
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.JwtTokenProvider
import com.hshar.eggegg.security.UserPrincipal
import findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserQuery @Autowired constructor(
        val userRepository: UserRepository,
        val authenticationManager: AuthenticationManager,
        val jwtTokenProvider: JwtTokenProvider) : GraphQLQueryResolver {


    fun getUser(id: String): User {
        return userRepository.findOne(id) ?: throw ResourceNotFoundException("User", "id", id)
    }

    fun myProfile(): User {
        return userRepository.findOne(getCurrentUser().getId())
                ?: throw ResourceNotFoundException("User", "id", getCurrentUser().getId())
    }

    fun signInUser(username: String, password: String): JwtAuthenticationResponse {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        username, password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtTokenProvider.generateToken(authentication)

        val user = userRepository.findByUsername(username)
                ?: throw ResourceNotFoundException("User", "username", username)

        return JwtAuthenticationResponse(
                accessToken = jwt,
                username = user.username,
                publicAddress = user.publicAddress,
                userAvatar = user.avatar,
                userId = user.id,
                roles = user.roles
        )
    }

    private fun getCurrentUser(): UserPrincipal {
        if (SecurityContextHolder.getContext().authentication.principal == "anonymousUser") {
            throw ResourceNotFoundException("Users", "Id", "anonymous")
        }
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
