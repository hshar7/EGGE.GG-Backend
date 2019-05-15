package com.hshar.eggegg.service

import com.hshar.eggegg.exception.ResourceNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.tx.ReadonlyTransactionManager
import org.web3j.tx.gas.StaticGasProvider
import java.util.*
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.model.permanent.Tournament
import com.hshar.eggegg.model.permanent.Web3Data
import com.hshar.eggegg.model.transient.type.TournamentStatus
import com.hshar.eggegg.repository.*
import com.hshar.eggegg.service.eventprocessor.TournamentIssuedProcessor
import findOne
import org.web3j.abi.datatypes.Address

@Service
class EventListenerService {

    @Autowired
    lateinit var web3j: Web3j

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var matchRepository: MatchRepository

    @Autowired
    lateinit var web3DataRepository: Web3DataRepository

    @Autowired
    lateinit var tournamentIssuedProcessor: TournamentIssuedProcessor

    companion object {
        const val CONTRACT_ADDRESS = "0xf09362eb76f310170a3874b6e16b416ddf28a7ed"
        const val GAS_PRICE = 200
        const val GAS_LIMIT = 4500000
    }

    // TODO: Implement reconnect logic for web socket
    // TODO: Implement a black list
    // TODO: Separate each event into own object. This boy getting too big.
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
        val fromBlock = web3DataRepository.findOne("TournamentIssued")?.fromBlock ?: 0.toBigInteger()

        tournamentsContract.tournamentIssuedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST
        ).subscribeWith(this.tournamentIssuedProcessor)
    }

    private fun contributionAddedEvent(tournamentsContract: Tournaments) {
        var fromBlockContributionAdded = 0.toBigInteger()
        web3DataRepository.findById("ContributionAdded").ifPresent {
            fromBlockContributionAdded = it.fromBlock
        }

        tournamentsContract.contributionAddedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlockContributionAdded),
                DefaultBlockParameterName.LATEST).subscribe {

            println("Tournament ${it._tournamentId} fund by ${it._contributor} for amount ${it._amount}.")

            val tournament = tournamentRepository.findByTournamentId(it._tournamentId.toInt())
                    .orElseThrow {
                        ResourceNotFoundException(Tournament::class.simpleName.toString(), "tournamentId", it._tournamentId)
                    }

            tournament.prize += it._amount.toBigDecimal()
            tournamentRepository.save(tournament)

            web3DataRepository.save(Web3Data(id = "ContributionAdded", fromBlock = it.log.blockNumber + 1.toBigInteger()))
        }
    }

    private fun tournamentDeadlineChangedEvent(tournamentsContract: Tournaments) {
        var fromBlockTournamentDeadlineChanged = 0.toBigInteger()
        web3DataRepository.findById("ContributionAdded").ifPresent {
            fromBlockTournamentDeadlineChanged = it.fromBlock
        }

        tournamentsContract.tournamentDeadlineChangedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlockTournamentDeadlineChanged),
                DefaultBlockParameterName.LATEST).subscribe {

            println("Tournament ${it._tournamentId} deadline change by ${it._changer} to ${it._deadline}.")

            val tournament = tournamentRepository.findByTournamentId(it._tournamentId.toInt())
                    .orElseThrow {
                        ResourceNotFoundException(Tournament::class.simpleName.toString(), "tournamentId", it._tournamentId)
                    }

            tournament.deadline = Date(it._deadline.toLong())

            web3DataRepository.save(Web3Data(id = "TournamentDeadlineChanged", fromBlock = it.log.blockNumber + 1.toBigInteger()))
        }
    }

    private fun tournamentFinalizedEvent(tournamentsContract: Tournaments) {

        var fromBlock = 0.toBigInteger()
        web3DataRepository.findById("TournamentFinalized").ifPresent {
            fromBlock = it.fromBlock
        }

        tournamentsContract.tournamentFinalizedEventFlowable(
                DefaultBlockParameter.valueOf(fromBlock),
                DefaultBlockParameterName.LATEST).subscribe {

            println("Tournament ${it._tournamentId} finalized event.")

            val tournament = tournamentRepository.findByTournamentId(it._tournamentId.toInt())
                    .orElseThrow {
                        ResourceNotFoundException(Tournament::class.simpleName.toString(), "tournamentId", it._tournamentId)
                    }

            tournament.winners = it._winners
            tournament.tournamentStatus = TournamentStatus.FINISHED
            if (tournament.matches.isNotEmpty()) {
                val match = tournament.matches.last()

                var address = ""
                for (d: Address in it._winners as List<Address>) {
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

            web3DataRepository.save(
                    Web3Data(id = "TournamentFinalized", fromBlock = it.log.blockNumber + 1.toBigInteger())
            )
        }
    }
}
