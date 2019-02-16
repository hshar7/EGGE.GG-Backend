package com.ethdenver.boulderbrackets.repository

import com.ethdenver.boulderbrackets.model.Game
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface GameRepository : MongoRepository<Game, String> {
    fun findByName(name: String): Optional<Game>
}
