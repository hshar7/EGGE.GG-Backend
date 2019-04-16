package com.hshar.eggegg.controller

import com.hshar.eggegg.model.Game
import com.hshar.eggegg.repository.GameRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class GamesController {

    @Autowired
    lateinit var gameRepository: GameRepository

    @GetMapping("/games")
    fun getAllGames(): List<Game> {
        return gameRepository.findAll()
    }
}
