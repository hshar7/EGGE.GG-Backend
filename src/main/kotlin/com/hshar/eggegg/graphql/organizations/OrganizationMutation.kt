package com.hshar.eggegg.graphql.organizations

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.mongo.Organization
import com.hshar.eggegg.model.permanent.mongo.User
import com.hshar.eggegg.repository.OrganizationRepository
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrganizationMutation : GraphQLMutationResolver {

    @Autowired
    lateinit var organizationRepository: OrganizationRepository

    @Autowired
    lateinit var userRepository: UserRepository

    fun createOrganization(organization: OrganizationInput): Organization {

        val owner: User = userRepository.findByPublicAddress(getCurrentUser().username)
                ?: throw ResourceNotFoundException("User", "publicAddress", getCurrentUser().username)

        val org = organizationRepository.insert(Organization(
                id = UUID.randomUUID().toString(),
                name = organization.name!!,
                owners = arrayListOf(owner),
                createdAt = Date(),
                updatedAt = Date()
        ))

        owner.organization = org
        userRepository.save(owner)
        return org
    }

    fun updateOrganization(organization: OrganizationInput): Organization {

        val organizationObj = organizationRepository.findById(organization.id!!)
                .orElseThrow { ResourceNotFoundException("Organization", "id", organization.id) }

        if (organization.description != null) {
            organizationObj.description = organization.description
        }
        if (organization.name != null) {
            organizationObj.name = organization.name
        }

        return organizationRepository.save(organizationObj)
    }

    private fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
