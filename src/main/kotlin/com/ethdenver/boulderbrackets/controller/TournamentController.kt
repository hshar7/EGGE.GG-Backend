package com.ethdenver.boulderbrackets.controller

import com.ethdenver.boulderbrackets.exception.ResourceNotFoundException
import com.ethdenver.boulderbrackets.model.Game
import com.ethdenver.boulderbrackets.model.Match
import com.ethdenver.boulderbrackets.model.Tournament
import com.ethdenver.boulderbrackets.model.User
import com.ethdenver.boulderbrackets.repository.GameRepository
import com.ethdenver.boulderbrackets.repository.MatchRepository
import com.ethdenver.boulderbrackets.repository.TournamentRepository
import com.ethdenver.boulderbrackets.repository.UserRepository
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mongodb.DBRef
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.web3j.protocol.Web3j
import java.util.*
import org.web3j.abi.TypeDecoder
import org.web3j.abi.datatypes.Address


@RestController
@RequestMapping("/api")
class TournamentController {
    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var gameRepository: GameRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var matchRepository: MatchRepository

    @Autowired
    lateinit var web3j: Web3j

    @PostMapping("/tournament")
    fun createTournament(@RequestBody request: String): ResponseEntity<String> {
        val tournamentData = Gson().fromJson<JsonObject>(request)

        val game = gameRepository.findByName(tournamentData["game"].asString)
            .orElseGet {
                gameRepository.insert(Game(id = UUID.randomUUID().toString(), name = tournamentData["game"].asString))
            }

        val user = userRepository.findById(tournamentData["userId"].asString)
            .orElseThrow { ResourceNotFoundException("User", "id", tournamentData["userId"].asString) }

        val response = this.web3j.ethGetTransactionReceipt(tournamentData["transactionHash"].asString).send()
        val data = response.transactionReceipt
            .orElseThrow { Exception("Transaction did not parse.") }

        val refMethod = TypeDecoder::class.java.getDeclaredMethod("decode", String::class.java, Int::class.java, Class::class.java)
        refMethod.isAccessible = true
        val address = refMethod.invoke(null, data.logs[0].data, 0, Address::class.java) as Address

        val tournament = tournamentRepository.insert(Tournament(
            id = UUID.randomUUID().toString(),
            name = tournamentData["name"].asString,
            description = tournamentData["description"].asString,
            maxPlayers = tournamentData["maxPlayers"].asInt,
            prize = tournamentData["prize"].asString,
            game = game,
            owner = user,
            participants = arrayListOf(),
            matches = Document(),
            contractAddress = address.toString()
        ))

        return ResponseEntity(Gson().toJson(tournament), HttpStatus.CREATED)
    }

    @GetMapping("/tournaments")
    fun getAllTournaments(): ResponseEntity<String> {
        return ResponseEntity(Gson().toJson(tournamentRepository.findAll()), HttpStatus.OK)
    }

    @GetMapping("/tournament/{id}")
    fun getTournament(@PathVariable("id") id: String): ResponseEntity<String> {
        val tournament = tournamentRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("tournament", "id", id) }

        tournament.matches.forEach {
            tournament.matches[it.key] = matchRepository.findById((it.value as DBRef).id.toString())
        }

        return ResponseEntity(Gson().toJson(tournament), HttpStatus.OK)
    }

    @PostMapping("/tournament/{tournamentId}/participant/{userId}")
    @Synchronized
    fun participate(
        @PathVariable("tournamentId") tournamentId: String,
        @PathVariable("userId") userId: String): ResponseEntity<String> {

        val tournament = tournamentRepository.findById(tournamentId)
            .orElseThrow { ResourceNotFoundException("tournament", "id", tournamentId) }

        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("User", "id", userId) }

        tournament.participants.add(user)

        if (tournament.participants.size == tournament.maxPlayers) {

            val matchesStack = Stack<Match>()
            val participants = tournament.participants.toMutableList()

            for (i in 1..tournament.maxPlayers - 1) {

                var match1: Match? = null
                var match2: Match? = null
                var player1: User? = null
                var player2: User? = null

                if (i <= tournament.maxPlayers / 2) {
                    player1 = participants[(0..participants.size - 1).random()]
                    participants.remove(player1)
                    player2 = participants[(0..participants.size - 1).random()]
                    participants.remove(player2)

                } else {
                    match2 = matchesStack.pop()
                    match1 = matchesStack.pop()
                }

                val match = Match(
                    id = UUID.randomUUID().toString(),
                    tournament = tournament,
                    player1 = player1,
                    player2 = player2,
                    winner = null,
                    match1 = match1,
                    match2 = match2
                )

                matchesStack.push(match)
                matchRepository.insert(match)
                tournament.matches[i.toString()] = match
            }
        }
        tournamentRepository.save(tournament)
        return ResponseEntity("{}", HttpStatus.OK) // TODO: Fix return here

    }
}
