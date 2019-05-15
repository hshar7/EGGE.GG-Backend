package com.hshar.eggegg.operation

import com.hshar.eggegg.model.permanent.Match
import com.hshar.eggegg.model.permanent.Tournament
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.repository.MatchRepository
import java.util.*

object TournamentOperations {
    fun generateBracket(tournament: Tournament, matchRepository: MatchRepository): Tournament {
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

        return tournament
    }
}
