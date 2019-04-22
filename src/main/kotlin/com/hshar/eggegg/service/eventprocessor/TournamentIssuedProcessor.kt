package com.hshar.eggegg.service.eventprocessor

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hshar.eggegg.contract.Tournaments
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.Game
import com.hshar.eggegg.model.Notification
import com.hshar.eggegg.model.Tournament
import com.hshar.eggegg.model.User
import com.hshar.eggegg.repository.GameRepository
import com.hshar.eggegg.repository.TokenRepository
import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import com.hshar.eggegg.service.NotificationService
import okhttp3.OkHttpClient
import okhttp3.Request
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.web3j.abi.datatypes.generated.Uint256
import java.util.*

@Service
class TournamentIssuedProcessor {

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

    fun process(eventData: Tournaments.TournamentIssuedEventResponse) {
        val user = userRepository.findByPublicAddress(eventData._organizer).orElseGet {
            userRepository.insert(User(
                    id = UUID.randomUUID().toString(),
                    publicAddress = eventData._organizer,
                    createdAt = Date(),
                    updatedAt = Date()
            ))
        }
        val request = Request.Builder().url("https://ipfs.infura.io:5001/api/v0/cat?arg=${eventData._data}").build()
        val data = OkHttpClient().newCall(request).execute().body()!!.string()
        val dataObj = Gson().fromJson<JsonObject>(data) // TODO: Create own data object

        val game = gameRepository.findByName(dataObj["game"].asString).orElseGet {
            gameRepository.insert(Game(
                    id = UUID.randomUUID().toString(),
                    name = dataObj["game"].asString,
                    createdAt = Date(),
                    updatedAt = Date()
            ))
        }

        val token = tokenRepository.findByAddress(eventData._token)
                .orElseThrow { ResourceNotFoundException("Token", "address", eventData._token) }

        val prizeDistInts: ArrayList<Int> = arrayListOf()
        for (d: Uint256 in eventData._prizeDistribution as List<Uint256>) {
            prizeDistInts.add(d.value.toInt())
        }

        val tournament = Tournament(
                id = UUID.randomUUID().toString(),
                tournamentId = eventData._tournamentId.toInt(),
                deadline = Date(eventData._deadline.toLong()),
                maxPlayers = eventData._maxPlayers.toInt(),
                prizeDistribution = prizeDistInts,
                winners = emptyList(),
                token = token,
                prize = 0.toBigInteger(),
                participants = arrayListOf(),
                game = game,
                matches = Document(),
                description = dataObj["description"].asString,
                name = dataObj["name"].asString,
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
    }
}
