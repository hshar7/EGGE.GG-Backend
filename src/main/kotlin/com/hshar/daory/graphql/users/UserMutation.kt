package com.hshar.daory.graphql.users

import className
import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.hshar.daory.exception.DuplicateResourceException
import com.hshar.daory.exception.ResourceNotFoundException
import com.hshar.daory.model.permanent.User
import com.hshar.daory.model.transient.type.RoleName
import com.hshar.daory.repository.RoleRepository
import com.hshar.daory.repository.UserRepository
import com.hshar.daory.security.UserPrincipal
import findOne
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserMutation @Autowired constructor(
        val userRepository: UserRepository,
        val roleRepository: RoleRepository,
        val passwordEncoder: PasswordEncoder,
        val authenticationManager: AuthenticationManager
) : GraphQLMutationResolver {

    private val logger = KotlinLogging.logger {}

    fun signUpUser(metadata: NewUserInput): User {
        // Check if user exists by public address and if password is "", then allow this user to own it
        val user = userRepository.findByPublicAddress(metadata.publicAddress)

        val role = roleRepository.findByName(RoleName.ROLE_PLAYER)
                ?: throw ResourceNotFoundException(roleRepository.className(), "name", RoleName.ROLE_PLAYER)

        if (user != null) {
            if (user.password != "") {
                throw DuplicateResourceException("User already exists. ${metadata.publicAddress}")
            } else {
                // Claim account to this user
                user.password = passwordEncoder.encode(metadata.password)
                user.email = metadata.email
                user.username = metadata.username
                user.name = metadata.name
                user.roles = mutableSetOf(role)
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
                roles = mutableSetOf(role),
                updatedAt = Date(),
                createdAt = Date()
        ))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun assignRoleToUser(roleName: RoleName, userId: String): User {
        val user = userRepository.findOne(userId)
                ?: throw ResourceNotFoundException(userRepository.className(), "id", userId)

        val role = roleRepository.findByName(roleName)
                ?: throw ResourceNotFoundException(roleRepository.className(), "name", roleName)

        user.roles.add(role)

        return userRepository.save(user)
    }

    fun updateMyMetadata(metadata: UpdateUserInput): User {
        val user = userRepository.findOne(getCurrentUser().getId())
                ?: throw ResourceNotFoundException("User", "id", getCurrentUser().getId())
        user.name = metadata.name ?: user.name
        user.username = metadata.username ?: user.username
        user.email = metadata.email ?: user.email

        return userRepository.save(user)
    }

    fun updateMyPassword(oldPassword: String, newPassword: String): Boolean {
        authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        getCurrentUser().username, oldPassword
                )
        )

        val user = userRepository.findByUsername(getCurrentUser().username)
                ?: throw ResourceNotFoundException("User", "username", getCurrentUser().username)

        user.password = passwordEncoder.encode(newPassword)

        userRepository.save(user)
        return true
    }

    private fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
