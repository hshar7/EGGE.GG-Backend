package com.hshar.eggegg.service.eventsubscriber

import className
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.Notification
import com.hshar.eggegg.model.transient.type.TournamentStatus
import com.hshar.eggegg.repository.*
import org.web3j.abi.datatypes.Address

class TournamentFinalizedSubscriber(
        private val tournamentRepository: TournamentRepository,
        private val matchRepository: MatchRepository,
        override var web3DataRepository: Web3DataRepository
) : GeneralEventSubscriber<Tournaments.TournamentFinalizedEventResponse>() {

    companion object {
        const val EVENT_NAME = "TournamentFinalized"
    }

    override val eventName = EVENT_NAME

    override fun onNext(eventData: Tournaments.TournamentFinalizedEventResponse) {
        try {
            logger.info("Tournament ${eventData._tournamentId} finalized event.")

            val tournament = tournamentRepository.findByTournamentId(eventData._tournamentId.toInt())
                    ?: throw ResourceNotFoundException(
                            tournamentRepository.className(), "tournamentId", eventData._tournamentId
                    )

            tournament.winners = eventData._winners
            tournament.tournamentStatus = TournamentStatus.FINISHED
            if (tournament.matches.isNotEmpty()) {
                val match = tournament.matches.last()

                var address = ""
                for (d: Address in eventData._winners as List<Address>) {
                    address = d.toString()
                    break
                }
                if (match.player1!!.publicAddress == address) {
                    match.winner = match.player1
                } else {
                    match.winner = match.player2
                }

                matchRepository.save(match)
            }
            tournamentRepository.save(tournament)

            this.proceedBlock(eventData.log.blockNumber)

        } catch (t: Exception) {
            logger.info("Retrying since encountered an exception ${t.localizedMessage}")
            this.onNext(eventData)
        } catch (t: Error) {
            logger.error(t.localizedMessage)
            this.proceedBlock(eventData.log.blockNumber)
        }
    }
}
