package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Token
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface TokenRepository : MongoRepository<Token, String> {
    fun findByAddress(address: String): Optional<Token>
}
