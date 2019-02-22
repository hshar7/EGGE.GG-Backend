package com.ethdenver.boulderbrackets.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService

@Configuration
class Web3jConfig {

    @Bean
    fun web3jProvider(): Web3j {
        val httpService = HttpService("https://rinkeby.infura.io/v3/ca86eb9dcddc4f85b7a3046c6f6b7b62")
        return Web3j.build(httpService)
    }
}
