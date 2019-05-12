package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Organization
import com.hshar.eggegg.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByPublicAddress(publicAddress: String): User?
    fun findByOrganizationId(organizationId: String): List<User>
    fun findByOrganization(organization: Organization): List<User>
}
