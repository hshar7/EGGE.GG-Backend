package com.hshar.eggegg.service.eventsubscriber

import className
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.Notification
import com.hshar.eggegg.repository.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeadlineChangedSubscriber : GeneralEventSubscriber<Tournaments.TournamentDeadlineChangedEventResponse>() {

    override val eventName = "DeadlineChanged"

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var matchRepository: MatchRepository

    @Autowired
    override lateinit var web3DataRepository: Web3DataRepository

    override fun onNext(eventData: Tournaments.TournamentDeadlineChangedEventResponse) {
        try {
            logger.info("Tournament ${eventData._tournamentId} deadline change by ${eventData._changer} to ${eventData._deadline}.")

            val tournament = tournamentRepository.findByTournamentId(eventData._tournamentId.toInt())
                    ?: throw ResourceNotFoundException(
                            tournamentRepository.className(), "tournamentId", eventData._tournamentId
                    )

            tournament.deadline = Date(eventData._deadline.toLong())

            proceedBlock(eventData.log.blockNumber)

        } catch (t: Exception) {
            logger.info("Retrying since encountered an exception ${t.localizedMessage}")
            onNext(eventData)
        } catch (t: Error) {
            logger.error(t.localizedMessage)
            proceedBlock(eventData.log.blockNumber)
        }
    }
}
