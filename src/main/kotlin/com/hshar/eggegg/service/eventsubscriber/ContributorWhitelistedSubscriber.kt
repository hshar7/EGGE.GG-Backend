package com.hshar.eggegg.service.eventsubscriber

import className
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.*

class ContributorWhitelistedSubscriber(
        private val tournamentRepository: TournamentRepository,
        override var web3DataRepository: Web3DataRepository,
        override var eventDataRepository: EventDataRepository,
        override var eventRetryDataRepository: EventRetryDataRepository
) : GeneralEventSubscriber<Tournaments.ContributorWhitelistedEventResponse>() {

    companion object {
        const val EVENT_NAME = "ContributorWhitelisted"
    }

    override val eventName = EVENT_NAME

    override fun onNext(eventData: Tournaments.ContributorWhitelistedEventResponse) {
        try {
            beforeNext(eventData.log.transactionHash)
            logger.info("Tournament ${eventData._tournamentId} contributor" +
                    " whitelisting ${eventData._contributor} event.")

            val tournament = tournamentRepository.findByTournamentId(eventData._tournamentId.toInt())
                    ?: throw ResourceNotFoundException(
                            tournamentRepository.className(), "tournamentId", eventData._tournamentId
                    )

            tournament.whitelistedContributors.add(eventData._contributor)

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
