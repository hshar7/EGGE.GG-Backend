package com.hshar.daory.graphql.leaderboard

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.daory.model.permanent.redis.Leaderboard
import com.hshar.daory.repository.LeaderboardRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LeaderboardQuery @Autowired constructor(
        val leaderboardRepository: LeaderboardRepository
) : GraphQLQueryResolver {
    fun getLeaderboard(): Iterable<Leaderboard> {
        return leaderboardRepository.findAll()
    }
}
