package com.hshar.eggegg.graphql.leaderboard

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.model.permanent.redis.Leaderboard
import com.hshar.eggegg.repository.LeaderboardRepository
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
