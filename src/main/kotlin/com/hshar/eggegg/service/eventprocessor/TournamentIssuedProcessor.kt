package com.hshar.eggegg.service.eventprocessor

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.Notification
import com.hshar.eggegg.model.permanent.Tournament
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.model.permanent.Web3Data
import com.hshar.eggegg.model.transient.payload.IpfsSchema
import com.hshar.eggegg.model.transient.type.TournamentStatus
import com.hshar.eggegg.repository.*
import com.hshar.eggegg.service.NotificationService
import findOne
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.internal.operators.flowable.FlowableError
import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.reactivestreams.Subscription
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.web3j.abi.datatypes.generated.Uint256
import reactor.core.Exceptions
import java.math.BigInteger
import java.util.*

@Service
class TournamentIssuedProcessor : FlowableSubscriber<Tournaments.TournamentIssuedEventResponse> {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var tokenRepository: TokenRepository

    @Autowired
    lateinit var notificationService: NotificationService

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var web3DataRepository: Web3DataRepository

    override fun onSubscribe(subscription: Subscription) {
        logger.info("Subscribed to TournamentIssuedEvent.")
        subscription.request(Long.MAX_VALUE)
    }

    override fun onNext(eventData: Tournaments.TournamentIssuedEventResponse) {
        try {
            logger.info("Tournament ${eventData._tournamentId} create by ${eventData._organizer}.")

            val user = userRepository.findByPublicAddress(eventData._organizer) ?: userRepository.insert(User(
                    id = UUID.randomUUID().toString(),
                    publicAddress = eventData._organizer,
                    organization = null,
                    createdAt = Date(),
                    updatedAt = Date()
            ))
            val request = Request.Builder().url("https://ipfs.infura.io:5001/api/v0/cat?arg=${eventData._data}").build()
            val data = OkHttpClient().newCall(request).execute().body()!!.string()
            val dataObj = Gson().fromJson<IpfsSchema>(data)

            val game = gameRepository.findOne(dataObj.gameId)
                    ?: throw ResourceNotFoundException("Game", "id", dataObj.gameId)

            val token = tokenRepository.findByAddress(eventData._token)
                    ?: throw ResourceNotFoundException("Token", "address", eventData._token)

            val prizeDistInts: ArrayList<Int> = arrayListOf()
            for (d: Uint256 in eventData._prizeDistribution as List<Uint256>) {
                prizeDistInts.add(d.value.toInt())
            }

            val tournament = Tournament(
                    id = UUID.randomUUID().toString(),
                    tournamentId = eventData._tournamentId.toInt(),
                    tournamentType = dataObj.tournamentType,
                    bracketType = dataObj.bracketType,
                    tournamentStatus = TournamentStatus.NEW,
                    tournamentFormat = dataObj.tournamentFormat,
                    deadline = Date(eventData._deadline.toLong()),
                    maxPlayers = eventData._maxPlayers.toInt(),
                    prizeDistribution = prizeDistInts,
                    winners = emptyList(),
                    token = token,
                    prize = 0.toBigDecimal(),
                    participants = arrayListOf(),
                    game = game,
                    matches = arrayListOf(),
                    description = dataObj.description,
                    name = dataObj.name,
                    owner = user,
                    createdAt = Date(),
                    updatedAt = Date()
            )

            tournamentRepository.insert(tournament)

            notificationService.newNotification(Notification(
                    id = UUID.randomUUID().toString(),
                    user = user,
                    url = "/tournament/${tournament.id}",
                    message = "${tournament.name} Created!",
                    createdAt = Date()
            ))

            this.proceedBlock(eventData.log.blockNumber)
        } catch (t: Exception) {
            logger.info("Retrying...")
            this.onNext(eventData)
        } catch (t: Error) {
            logger.error(t.localizedMessage)
            this.proceedBlock(eventData.log.blockNumber)
        }
    }

    override fun onComplete() {
        logger.info("Complete TournamentIssuedEvent.")
    }

    override fun onError(exception: Throwable) {
        logger.error(exception.localizedMessage)
    }

    private fun proceedBlock(blockNumber: BigInteger) {
        web3DataRepository.save(
                Web3Data(id = "TournamentIssued", fromBlock = blockNumber + 1.toBigInteger())
        )
    }
}
