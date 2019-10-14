package com.hshar.eggegg.graphql.users

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.mongo.User
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.UserPrincipal
import findOne
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserMutation : GraphQLMutationResolver {
    @Autowired
    lateinit var userRepository: UserRepository

    private val logger = KotlinLogging.logger {}

    fun metadata(metadata: UserInput): User {
        val user = userRepository.findOne(getCurrentUser().id)
                ?: throw ResourceNotFoundException("User", "id", getCurrentUser().id)
        user.name = metadata.name ?: user.name
        user.email = metadata.email ?: user.email

        return userRepository.save(user)
    }

    private fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
