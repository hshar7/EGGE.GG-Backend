package com.hshar.eggegg.graphql.organizations

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.Organization
import com.hshar.eggegg.repository.OrganizationRepository
import findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrganizationQuery : GraphQLQueryResolver {

    @Autowired
    lateinit var organizationRepository: OrganizationRepository

    fun getOrganization(id: String): Organization {
        return organizationRepository.findOne(id)
                ?: throw ResourceNotFoundException("Tournament", "id", id)
    }
}
