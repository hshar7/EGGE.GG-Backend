package com.hshar.daory.repository

import com.hshar.daory.model.permanent.Token
import org.springframework.data.mongodb.repository.MongoRepository

interface TokenRepository : MongoRepository<Token, String> {
    fun findByAddress(address: String): Token?
}
