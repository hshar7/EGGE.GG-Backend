package com.hshar.eggegg.graphql.tournaments

import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.Match
import com.hshar.eggegg.model.permanent.Tournament
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.model.transient.type.TournamentType
import com.hshar.eggegg.operation.TournamentOperations
import com.hshar.eggegg.repository.MatchRepository
import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.UserPrincipal
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.web3j.utils.Convert

@Service
class TournamentMutation : GraphQLMutationResolver {

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var matchRepository: MatchRepository

    private val logger = KotlinLogging.logger {}

    fun addParticipant(tournamentId: String, userId: String): Tournament {
        var tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow { ResourceNotFoundException("Tournament", "id", tournamentId) }

        when (tournament.tournamentType) {
            TournamentType.PRIZE_POOL, TournamentType.OFF_CHAIN -> logger.info("Adding user")
            else -> throw Exception("Cannot add a participant offchain to this Tournament ${tournament.id}.")
        }

        val user = userRepository.findById(userId)
                .orElseThrow { ResourceNotFoundException("User", "id", userId) }

        tournament.participants.add(user)

        if (tournament.participants.size == tournament.maxPlayers) {
            tournament = TournamentOperations.generateBracket(tournament, matchRepository)
        } else if (tournament.participants.size > tournament.maxPlayers) { return tournament }

        tournamentRepository.save(tournament)

        if (tournament.token.tokenVersion == 0) {
            tournament.prize = Convert.fromWei(tournament.prize, Convert.Unit.ETHER)
        }
        return tournament
    }

    fun matchWinner(pos: Int, matchId: String): List<Match> {

        val currentUser: User = userRepository.findByPublicAddress(getCurrentUser().username)
                ?: throw ResourceNotFoundException("User", "publicAddress", getCurrentUser().username)

        val match = matchRepository.findById(matchId)
            .orElseThrow { ResourceNotFoundException("Match", "id", matchId) }

        if ((match.tournament as Tournament).owner.publicAddress != currentUser.publicAddress) {
            return emptyList()
        }

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

    private fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
