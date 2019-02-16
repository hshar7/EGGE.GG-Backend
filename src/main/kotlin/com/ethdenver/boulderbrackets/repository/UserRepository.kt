package com.ethdenver.boulderbrackets.repository

import com.ethdenver.boulderbrackets.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun existsByPublicAddress(publicAddress: String): Boolean
    fun findByPublicAddress(publicAddress: String): User
}
