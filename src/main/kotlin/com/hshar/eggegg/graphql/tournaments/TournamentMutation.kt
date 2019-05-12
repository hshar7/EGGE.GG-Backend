package com.hshar.eggegg.graphql.tournaments

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.Match
import com.hshar.eggegg.model.Tournament
import com.hshar.eggegg.model.User
import com.hshar.eggegg.repository.MatchRepository
import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.web3j.utils.Convert
import java.util.*

@Service
class TournamentMutation : GraphQLMutationResolver {

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var matchRepository: MatchRepository

    fun addParticipant(tournamentId: String, userId: String): Tournament {
        val tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow { ResourceNotFoundException("Tournament", "id", tournamentId) }

        val user = userRepository.findById(userId)
                .orElseThrow { ResourceNotFoundException("User", "id", userId) }

        tournament.participants.add(user)

        if (tournament.participants.size == tournament.maxPlayers) {
            val matchesQueue: Queue<Match> = ArrayDeque<Match>()
            val participants = tournament.participants.toMutableList()

            for (i in 1..tournament.maxPlayers - 1) {

                var match1: Match? = null
                var match2: Match? = null
                var player1: User? = null
                var player2: User? = null

                if (i <= tournament.maxPlayers / 2) {
                    player1 = participants[(0..participants.size - 1).random()]
                    participants.remove(player1)
                    player2 = participants[(0..participants.size - 1).random()]
                    participants.remove(player2)

                } else {
                    match1 = matchesQueue.remove()
                    match2 = matchesQueue.remove()
                }

                val match = Match(
                    id = UUID.randomUUID().toString(),
                    tournament = tournament,
                    player1 = player1,
                    player2 = player2,
                    winner = null,
                    match1 = match1,
                    match2 = match2,
                    createdAt = Date(),
                    updatedAt = Date()
                )

                matchesQueue.add(match)
                tournament.matches.add(matchRepository.insert(match))
            }
        } else if (tournament.participants.size > tournament.maxPlayers) { /* No save */ }

        tournamentRepository.save(tournament)

        if (tournament.token.tokenVersion == 0) {
            tournament.prize = Convert.fromWei(tournament.prize, Convert.Unit.ETHER)
        }
        return tournament
    }

    fun matchWinner(pos: Int, matchId: String): List<Match> {
        val match = matchRepository.findById(matchId)
            .orElseThrow { ResourceNotFoundException("Match", "id", matchId) }

        if (pos == 1) {
            match.winner = match.player1
        } else {
            match.winner = match.player2
        }

        val newMatch = matchRepository.save(match)

        val matches = mutableListOf<Match>()
        newMatch.tournament.matches.forEach {thisMatch ->
            matches.add(thisMatch)

            var changed = false
            if (thisMatch.match1 != null) {
                if (thisMatch.match1.winner != null) {
                    thisMatch.player1 = thisMatch.match1.winner
                }
                changed = true
            }

            if (thisMatch.match2 != null) {
                if (thisMatch.match2.winner != null) {
                    thisMatch.player2 = thisMatch.match2.winner
                }

                changed = true
            }

            if (changed) {
                matchRepository.save(thisMatch)
            }
        }

        return matches
    }
}
