package com.hshar.eggegg.model.permanent.redis

import org.springframework.data.redis.core.RedisHash

@RedisHash("eventData")
data class EventData (
    val id: String
)
