package com.hshar.eggegg.model.permanent

import org.springframework.data.redis.core.RedisHash

@RedisHash("eventRetryData")
data class EventRetryData (
    val id: String,
    var retries: Int = 0
)
