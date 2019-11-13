package com.hshar.daory.graphql.games

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.daory.exception.ResourceNotFoundException
import com.hshar.daory.model.permanent.Game
import com.hshar.daory.repository.GameRepository
import findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class GamesQuery : GraphQLQueryResolver {

    @Autowired
    lateinit var gameRepository: GameRepository

    fun getGame(id: String): Game {
        return gameRepository.findOne(id) ?: throw ResourceNotFoundException("Game", "id", id)
    }

    fun getGames(count: Int, offset: Int): Page<Game> {
        val sort = Sort(Sort.Direction.ASC, "createdAt")
        return gameRepository.findAll(PageRequest.of(offset, count, sort))
    }
}
