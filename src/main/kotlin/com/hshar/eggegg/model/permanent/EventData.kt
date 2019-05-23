package com.hshar.eggegg.model.permanent

import org.springframework.data.redis.core.RedisHash

@RedisHash("eventData")
data class EventData (
    val id: String
)
