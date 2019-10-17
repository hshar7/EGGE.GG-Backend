package com.hshar.eggegg.graphql.tournaments

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.Tournament
import com.hshar.eggegg.repository.TournamentRepository
import findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.web3j.utils.Convert

@Service
class TournamentQuery : GraphQLQueryResolver {

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    fun getTournaments(count: Int, offset: Int): Page<Tournament> {
        val sort = Sort(Sort.Direction.DESC, "createdAt")
        return tournamentRepository.findAll(PageRequest.of(offset, count, sort))
    }

    fun getTournament(id: String): Tournament {
        val tournament = tournamentRepository.findOne(id) ?: throw ResourceNotFoundException("Tournament", "id", id)
        tournament.prize = Convert.fromWei(tournament.prize, Convert.Unit.ETHER)
        return tournament
    }

    fun findTournamentsByString(count: Int, offset: Int, fieldName: String, fieldData: Any): Page<Tournament> {
        val sort = Sort(Sort.Direction.DESC, "createdAt")
        return tournamentRepository.findByField(fieldName, fieldData, PageRequest.of(offset, count, sort))
    }

    fun findTournamentsByBool(count: Int, offset: Int, fieldName: String, fieldData: Any): Page<Tournament> {
        val sort = Sort(Sort.Direction.DESC, "createdAt")
        return tournamentRepository.findByField(fieldName, fieldData, PageRequest.of(offset, count, sort))
    }
}
