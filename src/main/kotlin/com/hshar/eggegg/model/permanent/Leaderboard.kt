package com.hshar.eggegg.model.permanent

import org.springframework.data.redis.core.RedisHash

@RedisHash("leaderboard")
data class Leaderboard (
        val id: String,
        val userId: String,
        var userName: String,
        val userPublicAddress: String,
        var earningsUSD: Float
)
