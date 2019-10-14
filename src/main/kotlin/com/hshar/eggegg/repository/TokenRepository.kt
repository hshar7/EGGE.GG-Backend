package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.mongo.Token
import org.springframework.data.mongodb.repository.MongoRepository

interface TokenRepository : MongoRepository<Token, String> {
    fun findByAddress(address: String): Token?
}
