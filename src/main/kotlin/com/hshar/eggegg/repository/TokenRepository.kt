package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.Token
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface TokenRepository : MongoRepository<Token, String> {
    fun findByAddress(address: String): Token?
}
