package com.hshar.eggegg.controller

import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.MatchRepository
import com.google.gson.Gson
import com.hshar.eggegg.model.Match
import com.mongodb.DBRef
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class MatchesController {
    @Autowired
    lateinit var matchRepository: MatchRepository

    @GetMapping("/match/{id}")
    fun getMatch(@PathVariable("id") id: String): ResponseEntity<String> {
        val match = matchRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Match", "id", id) }

        return ResponseEntity(Gson().toJson(match), HttpStatus.OK)
    }

    @PostMapping("/match/{id}/winner/{winner}")
    fun pickWinner(@PathVariable("id") id: String, @PathVariable("winner") winner: Int): ResponseEntity<String> {
        val match = matchRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Match", "id", id) }

        if (winner == 1) {
            match.winner = match.player1
        } else {
            match.winner = match.player2
        }

        val newMatch = matchRepository.save(match)

        val matches = mutableListOf<Match>()
        newMatch.tournament.matches.forEach {
            val thisMatch = matchRepository.findById((it.value as DBRef).id.toString())
                .orElseThrow { ResourceNotFoundException("Match", "id", (it.value as DBRef).id.toString()) }

            matches.add(thisMatch)

            var changed = false
            if (thisMatch.match1 != null) {
                if (thisMatch.match1.winner != null) {
                    thisMatch.player1 = thisMatch.match1.winner
                }
                changed = true
            }

            if (thisMatch.match2 != null) {
                if (thisMatch.match2.winner != null) {
                    thisMatch.player2 = thisMatch.match2.winner
                }

                changed = true
            }

            if (changed) {
                matchRepository.save(thisMatch)
            }
        }

        return ResponseEntity(Gson().toJson(matches), HttpStatus.OK)
    }
}
