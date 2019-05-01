package com.hshar.eggegg.graphql

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.Tournament
import com.hshar.eggegg.repository.GameRepository
import com.hshar.eggegg.repository.TournamentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.web3j.utils.Convert

@Service
class Query : GraphQLQueryResolver {

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    fun getTournaments(count: Int, offset: Int): Page<Tournament> {
        val sort = Sort(Sort.Direction.DESC, "createdAt")
        return tournamentRepository.findAll(PageRequest.of(offset, count, sort))
    }

    fun getTournament(id: String): Tournament {
        val tournament = tournamentRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Tournament", "id", id)
        }
        if (tournament.token.tokenVersion == 0) {
            tournament.prize = Convert.fromWei(tournament.prize.toBigDecimal(), Convert.Unit.ETHER).toBigInteger()
        }
        return tournament
    }

    fun findTournamentsByGameId(gameId: String): List<Tournament> {

        val game = gameRepository.findById(gameId).orElseThrow{
            ResourceNotFoundException("Game", "id", gameId)
        }

        return tournamentRepository.findByGame(game)
    }
}
