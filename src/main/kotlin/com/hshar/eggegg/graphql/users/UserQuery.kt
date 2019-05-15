package com.hshar.eggegg.graphql.users

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.repository.OrganizationRepository
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.UserPrincipal
import findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserQuery : GraphQLQueryResolver {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var organizationRepsitory: OrganizationRepository

    fun getUser(id: String): User {
        return userRepository.findOne(id) ?: throw ResourceNotFoundException("User", "id", id)
    }

    fun myProfile(): User {
        return userRepository.findOne(getCurrentUser().id)
                ?: throw ResourceNotFoundException("User", "id", getCurrentUser().id)
    }

    fun usersByOrganization(organizationId: String): List<User> {
        val org = organizationRepsitory.findOne(organizationId)
                ?: throw ResourceNotFoundException("Organization", "id", organizationId)
        return userRepository.findByOrganization(org)
    }

    private fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
