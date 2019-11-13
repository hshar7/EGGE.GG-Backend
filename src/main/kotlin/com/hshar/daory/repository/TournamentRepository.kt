package com.hshar.daory.repository

import com.hshar.daory.model.permanent.Tournament
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface TournamentRepository : MongoRepository<Tournament, String> {
    fun findByTournamentId(tournamentId: Int): Tournament?
    @Query("{ '?0' : ?1 }")
    fun findByField(fieldName: String, fieldData: Any, pageRequest: PageRequest): Page<Tournament>
}
