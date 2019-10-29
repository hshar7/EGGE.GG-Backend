package com.hshar.eggegg.graphql.users

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.hshar.eggegg.exception.DuplicateResourceException
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.UserPrincipal
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserMutation @Autowired constructor(
        val userRepository: UserRepository,
        val passwordEncoder: PasswordEncoder
) : GraphQLMutationResolver {

    private val logger = KotlinLogging.logger {}

//    fun metadata(metadata: NewUserInput): User {
//        val user = userRepository.findOne(getCurrentUser().getId())
//                ?: throw ResourceNotFoundException("User", "id", getCurrentUser().getId())
//        user.name = metadata.name ?: user.name
//        user.email = metadata.email ?: user.email
//
//        return userRepository.save(user)
//    }

    fun signUpUser(metadata: NewUserInput): User {
        // Check if user exists by public address and if password is "", then allow this user to own it
        val user = userRepository.findByPublicAddress(metadata.publicAddress)

        if (user != null) {
            if (user.password != "") {
                throw DuplicateResourceException("User already exists. ${metadata.publicAddress}")
            } else {
                // Claim account to this user
                user.password = passwordEncoder.encode(metadata.password)
                user.email = metadata.email
                user.username = metadata.username
                user.name = metadata.name
                user.updatedAt = Date()
                return userRepository.save(user)
            }
        }

        return userRepository.insert(User(
                id = UUID.randomUUID().toString(),
                publicAddress = metadata.publicAddress,
                password = passwordEncoder.encode(metadata.password),
                username = metadata.username,
                name = metadata.name,
                email = metadata.email,
                updatedAt = Date(),
                createdAt = Date()
        ))
    }

    private fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
