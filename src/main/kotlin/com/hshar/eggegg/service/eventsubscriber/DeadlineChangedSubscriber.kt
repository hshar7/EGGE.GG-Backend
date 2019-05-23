package com.hshar.eggegg.service.eventsubscriber

import className
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeadlineChangedSubscriber(
        private val tournamentRepository: TournamentRepository,
        override var web3DataRepository: Web3DataRepository,
        override var eventDataRepository: EventDataRepository
) : GeneralEventSubscriber<Tournaments.TournamentDeadlineChangedEventResponse>() {

    companion object {
        const val EVENT_NAME = "DeadlineChanged"
    }
    override val eventName = EVENT_NAME

    override fun onNext(eventData: Tournaments.TournamentDeadlineChangedEventResponse) {
        try {
            beforeNext(eventData.log.transactionHash)
            logger.info("Tournament ${eventData._tournamentId} deadline change by ${eventData._changer} to ${eventData._deadline}.")

            val tournament = tournamentRepository.findByTournamentId(eventData._tournamentId.toInt())
                    ?: throw ResourceNotFoundException(
                            tournamentRepository.className(), "tournamentId", eventData._tournamentId
                    )

            tournament.deadline = Date(eventData._deadline.toLong())

            proceedBlock(eventData.log.blockNumber, eventData.log.transactionHash)

        } catch (t: Exception) {
            logger.info("Retrying since encountered an exception ${t.localizedMessage}")
            onNext(eventData)
        } catch (t: Error) {
            logger.error(t.localizedMessage)
            proceedBlock(eventData.log.blockNumber, eventData.log.transactionHash)
        }
    }
}
