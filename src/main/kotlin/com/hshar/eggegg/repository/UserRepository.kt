package com.hshar.eggegg.repository

import com.hshar.eggegg.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository : MongoRepository<User, String> {
    fun findByPublicAddress(publicAddress: String): Optional<User>
}
