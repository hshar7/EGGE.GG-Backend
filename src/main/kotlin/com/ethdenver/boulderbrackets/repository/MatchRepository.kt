package com.ethdenver.boulderbrackets.repository

import com.ethdenver.boulderbrackets.model.Match
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRepository: MongoRepository<Match, String> {
}
