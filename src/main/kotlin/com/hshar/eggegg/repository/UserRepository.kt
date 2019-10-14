package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.mongo.Organization
import com.hshar.eggegg.model.permanent.mongo.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByPublicAddress(publicAddress: String): User?
    fun findByOrganization(organization: Organization): List<User>
}
