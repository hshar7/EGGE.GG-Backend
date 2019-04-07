package com.hshar.eggegg.service

import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.Game
import com.hshar.eggegg.model.Tournament
import com.hshar.eggegg.model.User
import com.hshar.eggegg.model.Web3Data
import com.hshar.eggegg.repository.GameRepository
import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.repository.Web3DataRepository
import com.github.kittinunf.fuel.httpGet
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.ipfs.kotlin.defaults.InfuraIPFS
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.tx.ReadonlyTransactionManager
import org.web3j.tx.gas.StaticGasProvider
import java.util.*
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.get

@Service
class EventListenerService {

    @Autowired
    lateinit var web3j: Web3j

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var web3DataRepository: Web3DataRepository

    companion object {
        const val CONTRACT_ADDRESS = "0x4c94f4ac7f93bd6f145134eae9e127de09176c1d"
        const val GAS_PRICE = 200
        const val GAS_LIMIT = 4500000
    }

    // TODO: Implement reconnect logic for web socket
    // TODO: Implement a bounty black list
    // TODO: Parse data object
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
    }

    private fun tournamentDeadlineChangedEvent(tournamentsContract: Tournaments) {
        var fromBlockTournamentDeadlineChanged = 0.toBigInteger()
        web3DataRepository.findById("ContributionAdded").ifPresent {
            fromBlockTournamentDeadlineChanged = it.fromBlock
        }

        tournamentsContract.tournamentDeadlineChangedEventFlowable(
            DefaultBlockParameter.valueOf(fromBlockTournamentDeadlineChanged),
            DefaultBlockParameterName.LATEST).subscribe {

            println("Tournament ${it._tournamentId} deadline changed by ${it._changer} to ${it._deadline}.")

            val tournament = tournamentRepository.findByTournamentId(it._tournamentId.toInt())
                .orElseThrow {
                    ResourceNotFoundException(Tournament::class.simpleName.toString(), "tournamentId", it._tournamentId)
                }

            tournament.deadline = Date(it._deadline.toLong())

            web3DataRepository.save(Web3Data(id = "TournamentDeadlineChanged", fromBlock = it.log.blockNumber + 1.toBigInteger()))
        }
    }

    private fun contributionAddedEvent(tournamentsContract: Tournaments) {
        var fromBlockContributionAdded = 0.toBigInteger()
        web3DataRepository.findById("ContributionAdded").ifPresent {
            fromBlockContributionAdded = it.fromBlock
        }

        tournamentsContract.contributionAddedEventFlowable(
            DefaultBlockParameter.valueOf(fromBlockContributionAdded),
            DefaultBlockParameterName.LATEST).subscribe {

            println("Tournament ${it._tournamentId} funded by ${it._contributor} for amount ${it._amount}.")

            val tournament = tournamentRepository.findByTournamentId(it._tournamentId.toInt())
                .orElseThrow {
                    ResourceNotFoundException(Tournament::class.simpleName.toString(), "tournamentId", it._tournamentId)
                }

            tournament.prize += it._amount.toInt()
            tournamentRepository.save(tournament)

            web3DataRepository.save(Web3Data(id = "ContributionAdded", fromBlock = it.log.blockNumber + 1.toBigInteger()))
        }
    }

    private fun tournamentIssuedEvent(tournamentsContract: Tournaments) {

        var fromBlockTournamentIssued = 0.toBigInteger()
        web3DataRepository.findById("TournamentIssued").ifPresent {
            fromBlockTournamentIssued = it.fromBlock
        }

        tournamentsContract.tournamentIssuedEventFlowable(
            DefaultBlockParameter.valueOf(fromBlockTournamentIssued),
            DefaultBlockParameterName.LATEST).subscribe{

            println("Tournament ${it._tournamentId} create by ${it._organizer}.")

            val user = userRepository.findByPublicAddress(it._organizer).orElseGet {
                userRepository.insert(User(
                    id = UUID.randomUUID().toString(),
                    publicAddress = it._organizer
                ))
            }

            val data = InfuraIPFS().get.cat(it._data)
            val dataObj = Gson().fromJson<JsonObject>(data) // TODO: Create own data object

            val game = gameRepository.findByName(dataObj["game"].asString).orElseGet {
                gameRepository.insert(Game(
                    id = UUID.randomUUID().toString(),
                    name = dataObj["game"].asString
                ))
            }

            var tokenName = "ETH"
            var tokenPrice = 0.toFloat()
            if (it._tokenVersion == 20.toBigInteger()) {

                val (_, _, result) =
                    "http://api.ethplorer.io/getTokenInfo/${it._token}/?apiKey=freekey".httpGet().responseString()

                when (result) {
                    is Result.Failure -> {
                        throw result.getException()
                    }
                    is Result.Success -> {
                        val resultObject = Gson().fromJson<JsonObject>(result.get())
                        tokenName = resultObject["symbol"].toString()
                        tokenPrice = resultObject["price"]["rate"].asFloat
                    }
                }
            } else {
                val (_, _, result) = "https://api.coincap.io/v2/assets/ethereum".httpGet().responseString()

                when (result) {
                    is Result.Failure -> {
                        throw result.getException()
                    }
                    is Result.Success -> {
                        val resultObject = Gson().fromJson<JsonObject>(result.get())
                        tokenPrice = resultObject["data"]["priceUsd"].asFloat
                    }
                }
            }

            tournamentRepository.insert(Tournament(
                id = UUID.randomUUID().toString(),
                tournamentId = it._tournamentId.toInt(),
                deadline = Date(it._deadline.toLong()),
                tokenVersion = it._tokenVersion.toInt(),
                token = it._token,
                tokenName = tokenName,
                tokenPrice = tokenPrice,
                prize = 0,
                participants = arrayListOf(),
                game = game,
                matches = Document(),
                description = dataObj["description"].asString,
                maxPlayers = dataObj["maxPlayers"].asInt,
                name = dataObj["name"].asString,
                owner = user
            ))

            web3DataRepository.save(Web3Data(id = "TournamentIssued", fromBlock = it.log.blockNumber + 1.toBigInteger()))
        }
    }
}
