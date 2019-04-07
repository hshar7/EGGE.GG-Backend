package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Game
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface GameRepository : MongoRepository<Game, String> {
    fun findByName(name: String): Optional<Game>
}
