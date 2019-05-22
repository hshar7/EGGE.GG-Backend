package com.hshar.eggegg.service

import org.springframework.beans.factory.annotation.Autowired
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
import io.reactivex.subscribers.DisposableSubscriber
import org.apache.commons.collections4.collection.SynchronizedCollection


@Service
final class EventListenerService @Autowired constructor(
        private val web3DataRepository: Web3DataRepository,
        private val userRepository: UserRepository,
        private val gameRepository: GameRepository,
        private val tournamentRepository: TournamentRepository,
        private val tokenRepository: TokenRepository,
        private val notificationService: NotificationService,
        private val matchRepository: MatchRepository,
        private val web3j: Web3j
) {
    companion object {
        const val CONTRACT_ADDRESS = "0xf09362eb76f310170a3874b6e16b416ddf28a7ed"
        const val GAS_PRICE = 200
        const val GAS_LIMIT = 4500000
    }

    private val subscribers: SynchronizedCollection<DisposableSubscriber<out Any>> =
            SynchronizedCollection.synchronizedCollection(arrayListOf())

    init {
        initSubscribers()
    }

    // TODO: Implement a black list
    // TODO: Add a way to prevent processing of multiple messages. Using Redis?
    @Synchronized
    fun initSubscribers() {

        val transactionManager = ReadonlyTransactionManager(web3j, "0x0")
        val gasProvider = StaticGasProvider(GAS_PRICE.toBigInteger(), GAS_LIMIT.toBigInteger())

        val tournamentsContract = Tournaments.load(
                CONTRACT_ADDRESS,
                web3j,
                transactionManager,
                gasProvider
        )

        if (!subscribers.isEmpty()) {
            for (i in 0 until subscribers.size) { // Has to be done this way to not use the concurrent iterator
                val sub: DisposableSubscriber<out Any> = subscribers.toList()[i]
                if (!sub.isDisposed) sub.dispose()
            }
        }
        subscribers.clear()

        subscribers.add(tournamentIssuedEvent(tournamentsContract))
        subscribers.add(contributionAddedEvent(tournamentsContract))
        subscribers.add(tournamentDeadlineChangedEvent(tournamentsContract))
        subscribers.add(tournamentFinalizedEvent(tournamentsContract))
    }

    private fun tournamentIssuedEvent(tournamentsContract: Tournaments)
            : DisposableSubscriber<Tournaments.TournamentIssuedEventResponse> {
        val fromBlock = web3DataRepository.findOne(TournamentIssuedSubscriber.EVENT_NAME)?.fromBlock ?: 0.toBigInteger()

        return tournamentsContract.tournamentIssuedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST
        ).subscribeWith(TournamentIssuedSubscriber(
                userRepository,
                gameRepository,
                tournamentRepository,
                tokenRepository,
                notificationService,
                web3DataRepository
        ))
    }

    private fun contributionAddedEvent(tournamentsContract: Tournaments)
            : DisposableSubscriber<Tournaments.ContributionAddedEventResponse> {
        val fromBlock = web3DataRepository.findOne(ContributionAddedSubscriber.EVENT_NAME)?.fromBlock
                ?: 0.toBigInteger()

        return tournamentsContract.contributionAddedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST
        ).subscribeWith(ContributionAddedSubscriber(
                tournamentRepository,
                matchRepository,
                userRepository,
                web3DataRepository
        ))
    }

    private fun tournamentDeadlineChangedEvent(tournamentsContract: Tournaments)
            : DisposableSubscriber<Tournaments.TournamentDeadlineChangedEventResponse> {
        val fromBlock = web3DataRepository.findOne(DeadlineChangedSubscriber.EVENT_NAME)?.fromBlock ?: 0.toBigInteger()

        return tournamentsContract.tournamentDeadlineChangedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST
        ).subscribeWith(DeadlineChangedSubscriber(
                tournamentRepository,
                web3DataRepository
        ))
    }

    private fun tournamentFinalizedEvent(tournamentsContract: Tournaments)
            : DisposableSubscriber<Tournaments.TournamentFinalizedEventResponse> {
        val fromBlock = web3DataRepository.findOne(TournamentFinalizedSubscriber.EVENT_NAME)?.fromBlock
                ?: 0.toBigInteger()

        return tournamentsContract.tournamentFinalizedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST
        ).subscribeWith(TournamentFinalizedSubscriber(
                tournamentRepository,
                matchRepository,
                web3DataRepository
        ))
    }
}
