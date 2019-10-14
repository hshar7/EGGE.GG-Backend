package com.hshar.eggegg.graphql.tournaments

import className
import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.exception.UnauthorizedException
import com.hshar.eggegg.model.permanent.mongo.Match
import com.hshar.eggegg.model.permanent.mongo.Notification
import com.hshar.eggegg.model.permanent.mongo.Tournament
import com.hshar.eggegg.model.permanent.mongo.User
import com.hshar.eggegg.model.transient.type.BracketType
import com.hshar.eggegg.model.transient.type.TournamentStatus
import com.hshar.eggegg.model.transient.type.TournamentType
import com.hshar.eggegg.operation.TournamentOperations
import com.hshar.eggegg.repository.MatchRepository
import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.security.UserPrincipal
import com.hshar.eggegg.service.NotificationService
import findOne
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
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

    @Autowired
    lateinit var notificationService: NotificationService

    private val logger = KotlinLogging.logger {}

    fun addParticipant(tournamentId: String, userId: String): Tournament {
        val tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow { ResourceNotFoundException("Tournament", "id", tournamentId) }

        when (tournament.tournamentType) {
            TournamentType.PRIZE_POOL, TournamentType.OFF_CHAIN -> logger.info("Adding user")
            else -> throw Exception("Cannot add a participant offchain to this Tournament ${tournament.id}.")
        }
        if (tournament.tournamentStatus != TournamentStatus.NEW) {
            throw Exception("Cannot add a new participant anymore to this Tournament ${tournament.id}.")
        }

        val user = userRepository.findOne(userId) ?: throw ResourceNotFoundException("User", "id", userId)
        tournament.participants.add(user)
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

//        if ((match.tournament as Tournament).owner.publicAddress != currentUser.publicAddress) {
//            return emptyList()
//        }

        if (pos == 1) {
            match.winner = match.player1
        } else {
            match.winner = match.player2
        }

        val newMatch = matchRepository.save(match)

        val matches = mutableListOf<Match>()
        newMatch.tournament.matches.forEach { thisMatch ->
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

    fun roundUpdate(tournamentId: String, roundNumber: Int, round: MutableMap<String, Int>): Tournament {
        val currentUser: User = userRepository.findByPublicAddress(getCurrentUser().username)
                ?: throw ResourceNotFoundException("User", "publicAddress", getCurrentUser().username)

        val tournament = tournamentRepository.findOne(tournamentId)
                ?: throw ResourceNotFoundException(tournamentRepository.className(), "id", tournamentId)

        if (tournament.owner.publicAddress != currentUser.publicAddress) {
            throw UnauthorizedException("User is not owner of this tournament.")
        }

        tournament.rounds[roundNumber] = round

        // Check for winners
        val totals = hashMapOf<String, Int>()
        tournament.rounds.forEach {
            it.forEach { (userId, points) ->
                totals[userId] = totals[userId]?.plus(points) ?: points
                if (totals[userId]!! >= tournament.pointsToWin) {
                    tournament.tournamentStatus = TournamentStatus.FINISHED
                }
            }
        }
        // Check for final round
        if (roundNumber >= tournament.numberOfRounds) tournament.tournamentStatus = TournamentStatus.FINISHED

        if (tournament.tournamentStatus == TournamentStatus.FINISHED) {
            totals.toList().sortedBy { (_, value) -> value }.reversed().toMap().forEach { (userId, _) ->
                val winner = userRepository.findOne(userId)
                        ?: throw ResourceNotFoundException(userRepository.className(), "id", userId)
                tournament.winners.add(winner.publicAddress)
            }
            notificationService.newNotification(Notification(
                    id = UUID.randomUUID().toString(),
                    user = tournament.owner,
                    url = "/tournament/${tournament.id}",
                    message = "${tournament.name} Finished, please issue payments now!",
                    createdAt = Date()
            ))
        }

        return tournamentRepository.save(tournament)
    }

    fun startTournament(tournamentId: String): Tournament {
        var tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow { ResourceNotFoundException("Tournament", "id", tournamentId) }

        tournament.tournamentStatus = TournamentStatus.LIVE
        if (tournament.bracketType == BracketType.BATTLE_ROYALE) {
            for (i in 0 until tournament.numberOfRounds) {
                val standings = hashMapOf<String, Int>()
                tournament.participants.forEach {
                    standings[it.id] = 0
                }
                tournament.rounds.add(standings)
            }
        } else { // Single Elimination
            tournament = TournamentOperations.generateBracket(tournament, matchRepository)
        }

        tournamentRepository.save(tournament)

        if (tournament.token.tokenVersion == 0) {
            tournament.prize = Convert.fromWei(tournament.prize, Convert.Unit.ETHER)
        }

        return tournament
    }

    private fun getCurrentUser(): UserPrincipal {
        return SecurityContextHolder.getContext().authentication.principal as UserPrincipal
    }
}
