package com.hshar.eggegg.service.eventsubscriber

import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.Tournament
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.model.transient.type.TournamentType
import com.hshar.eggegg.operation.TournamentOperations
import com.hshar.eggegg.repository.*
import java.util.*

class ContributionAddedSubscriber(
        private val tournamentRepository: TournamentRepository,
        private val matchRepository: MatchRepository,
        private val userRepository: UserRepository,
        override var web3DataRepository: Web3DataRepository,
        override var eventDataRepository: EventDataRepository,
        override var eventRetryDataRepository: EventRetryDataRepository
) : GeneralEventSubscriber<Tournaments.ContributionAddedEventResponse>() {

    companion object {
        const val EVENT_NAME = "ContributionAdded"
    }
    override var eventName = EVENT_NAME

    override fun onNext(eventData: Tournaments.ContributionAddedEventResponse) {
        try {
            beforeNext(eventData.log.transactionHash)
            logger.info("Tournament ${eventData._tournamentId} fund" +
                            " by ${eventData._contributor} for amount ${eventData._amount}.")

            var tournament = tournamentRepository.findByTournamentId(eventData._tournamentId.toInt())
                    ?: throw ResourceNotFoundException(
                            Tournament::class.simpleName.toString(), "tournamentId", eventData._tournamentId
                    )

            if (tournament.tournamentType == TournamentType.BUY_IN) {
                val user = userRepository.findByPublicAddress(eventData._contributor) ?: userRepository.insert(User(
                        id = UUID.randomUUID().toString(),
                        publicAddress = eventData._contributor,
                        username = eventData._contributor,
                        email = eventData._contributor,
                        password = "", // TODO: These need proper values.. whole thing needs rework.
                        createdAt = Date(),
                        updatedAt = Date()
                ))
                tournament.participants.add(user)

                if (tournament.participants.size == tournament.maxPlayers) {
                    tournament = TournamentOperations.generateBracket(tournament, matchRepository)
                }
            }

            tournament.prize += eventData._amount.toBigDecimal()
            tournamentRepository.save(tournament)

            this.proceedBlock(eventData.log.blockNumber, eventData.log.transactionHash)
        } catch (t: Exception) {
            if (needsRetry(eventData.log.transactionHash)) {
                logger.warn("Retrying since encountered an exception ${t.localizedMessage}", t)
                Thread.sleep(1000)
                this.onNext(eventData)
            } else {
                this.proceedBlock(eventData.log.blockNumber, eventData.log.transactionHash)
            }
        } catch (t: Error) {
            logger.error(t.localizedMessage, t)
            this.proceedBlock(eventData.log.blockNumber, eventData.log.transactionHash)
        }
    }
}
