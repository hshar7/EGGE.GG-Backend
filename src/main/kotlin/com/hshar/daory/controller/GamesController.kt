package com.hshar.daory.controller

import com.hshar.daory.model.permanent.Game
import com.hshar.daory.repository.GameRepository
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
