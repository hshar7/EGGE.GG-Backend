package com.hshar.eggegg.cron

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.get
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hshar.eggegg.repository.TokenRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TokenPricesCronTask {

    protected val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var tokenRepository: TokenRepository

    @Scheduled(fixedRate = 3600000)
    fun updateTokenPrices() {
        tokenRepository.findAll().forEach {token ->
            if (token.tokenVersion == 20) {
                val (_, _, result) =
                        "http://api.ethplorer.io/getTokenInfo/${token.address}/?apiKey=freekey".httpGet().responseString()

                when (result) {
                    is Result.Failure -> {
                        throw result.getException()
                    }
                    is Result.Success -> {
                        val resultObject = Gson().fromJson<JsonObject>(result.get())
                        if (resultObject["price"] != null) {
                            token.usdPrice = resultObject["price"]["rate"].asFloat
                            logger.info("Updated token ${token.symbol} price to ${token.usdPrice}")
                            tokenRepository.save(token)
                        }
                    }
                }
            } else if (token.tokenVersion == 0) {
                val (_, _, result) = "https://api.coincap.io/v2/assets/ethereum".httpGet().responseString()

                when (result) {
                    is Result.Failure -> {
                        throw result.getException()
                    }
                    is Result.Success -> {
                        val resultObject = Gson().fromJson<JsonObject>(result.get())
                        token.usdPrice = resultObject["data"]["priceUsd"].asFloat
                        logger.info("Updated ETH price to ${token.usdPrice}")
                        tokenRepository.save(token)
                    }
                }
            }
        }
    }
}
