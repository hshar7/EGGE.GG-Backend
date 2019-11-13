package com.hshar.daory.repository

import com.hshar.daory.model.permanent.Game
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface GameRepository : MongoRepository<Game, String> {
    fun findByName(name: String): Optional<Game>
}
