package com.hshar.eggegg.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.tx.ReadonlyTransactionManager
import org.web3j.tx.gas.StaticGasProvider
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.repository.*
import com.hshar.eggegg.service.eventsubscriber.*
import findOne

@Service
final class EventListenerService @Autowired constructor(
        private val deadlineChangedSubscriber: DeadlineChangedSubscriber,
        private val tournamentFinalizedSubscriber: TournamentFinalizedSubscriber,
        private val contributionAddedSubscriber: ContributionAddedSubscriber,
        private val tournamentIssuedSubscriber: TournamentIssuedSubscriber,
        private val web3DataRepository: Web3DataRepository,
        private val web3j: Web3j
) {
    companion object {
        const val CONTRACT_ADDRESS = "0xf09362eb76f310170a3874b6e16b416ddf28a7ed"
        const val GAS_PRICE = 200
        const val GAS_LIMIT = 4500000
    }


    // TODO: Implement reconnect logic for web socket
    // TODO: Implement a black list
    // TODO: Add a way to prevent processing of multiple messages. Using Redis?
    @Bean
    fun listenToContractEvents() {

        val transactionManager = ReadonlyTransactionManager(web3j, "0x0")
        val gasProvider = StaticGasProvider(GAS_PRICE.toBigInteger(), GAS_LIMIT.toBigInteger())

        val tournamentsContract = Tournaments.load(
                CONTRACT_ADDRESS,
                web3j,
                transactionManager,
                gasProvider
        )

        tournamentIssuedEvent(tournamentsContract)
        contributionAddedEvent(tournamentsContract)
        tournamentDeadlineChangedEvent(tournamentsContract)
        tournamentFinalizedEvent(tournamentsContract)
    }

    private fun tournamentIssuedEvent(tournamentsContract: Tournaments) {
        val fromBlock = web3DataRepository.findOne(tournamentIssuedSubscriber.eventName)?.fromBlock ?: 0.toBigInteger()

        tournamentsContract.tournamentIssuedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST
        ).subscribeWith(tournamentIssuedSubscriber)
    }

    private fun contributionAddedEvent(tournamentsContract: Tournaments) {
        val fromBlock = web3DataRepository.findOne(contributionAddedSubscriber.eventName)?.fromBlock ?: 0.toBigInteger()

        tournamentsContract.contributionAddedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST
        ).subscribeWith(contributionAddedSubscriber)
    }

    private fun tournamentDeadlineChangedEvent(tournamentsContract: Tournaments) {
        val fromBlock = web3DataRepository.findOne(deadlineChangedSubscriber.eventName)?.fromBlock
                ?: 0.toBigInteger()

        tournamentsContract.tournamentDeadlineChangedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST).subscribeWith(deadlineChangedSubscriber)
    }

    private fun tournamentFinalizedEvent(tournamentsContract: Tournaments) {
        val fromBlock = web3DataRepository.findOne(tournamentFinalizedSubscriber.eventName)?.fromBlock
                ?: 0.toBigInteger()

        tournamentsContract.tournamentFinalizedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST).subscribeWith(tournamentFinalizedSubscriber)
    }
}
