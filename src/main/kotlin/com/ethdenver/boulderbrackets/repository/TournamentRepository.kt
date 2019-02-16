package com.ethdenver.boulderbrackets.repository

import com.ethdenver.boulderbrackets.model.Tournament
import org.springframework.data.mongodb.repository.MongoRepository

interface TournamentRepository : MongoRepository<Tournament, String> {
}
