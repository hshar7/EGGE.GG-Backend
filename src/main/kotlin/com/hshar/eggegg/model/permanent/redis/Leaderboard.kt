package com.hshar.eggegg.model.permanent.redis

import org.springframework.data.redis.core.RedisHash

@RedisHash("leaderboard")
data class Leaderboard (
        val id: String,
        val userId: String,
        var userName: String,
        var avatar: String,
        val userPublicAddress: String,
        var earningsUSD: Float
)
