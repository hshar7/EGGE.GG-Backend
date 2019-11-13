package com.hshar.daory.repository

import com.hshar.daory.model.permanent.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByPublicAddress(publicAddress: String): User?
    fun findByUsername(username: String): User?
}
