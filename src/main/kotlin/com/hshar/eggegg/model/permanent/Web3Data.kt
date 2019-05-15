package com.hshar.eggegg.model.permanent

import org.springframework.data.redis.core.RedisHash
import java.math.BigInteger

@RedisHash("web3data")
data class Web3Data(
    val id: String,
    var fromBlock: BigInteger
)
