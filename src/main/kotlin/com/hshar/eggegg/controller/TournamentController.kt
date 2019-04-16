package com.hshar.eggegg.controller

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.set
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.Match
import com.hshar.eggegg.model.User
import com.hshar.eggegg.repository.GameRepository
import com.hshar.eggegg.repository.MatchRepository
import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mongodb.DBRef
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.web3j.utils.Convert
import org.web3j.utils.Convert.fromWei
import java.util.*

@RestController
@RequestMapping("/api")
class TournamentController {
    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var matchRepository: MatchRepository

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

        if (tournament.token.tokenVersion == 0) {
            val jsonObj = Gson().fromJson<JsonObject>(Gson().toJson(tournament))
            jsonObj["prize"] = fromWei(tournament.prize.toBigDecimal(), Convert.Unit.ETHER)
            return ResponseEntity(jsonObj.toString(), HttpStatus.OK)
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
            val matchesQueue: Queue<Match> = ArrayDeque<Match>()
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
                    match1 = matchesQueue.remove()
                    match2 = matchesQueue.remove()
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

                matchesQueue.add(match)
                matchRepository.insert(match)
                tournament.matches[i.toString()] = match
            }
        }

        tournamentRepository.save(tournament)

        return ResponseEntity(
            "{\"status\": \"ok\"}", // TODO: Still need to fix this!
            HttpStatus.OK
        )
    }
}