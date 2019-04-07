package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Tournament
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface TournamentRepository : MongoRepository<Tournament, String> {
    fun findByTournamentId(tournamentId: Int): Optional<Tournament>
}
