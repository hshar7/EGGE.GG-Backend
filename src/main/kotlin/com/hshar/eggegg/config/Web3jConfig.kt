package com.hshar.eggegg.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.websocket.WebSocketService

@Configuration
class Web3jConfig {

    @Bean
    fun web3jProvider(): Web3j {
        val wssService = WebSocketService(
            "wss://rinkeby.infura.io/ws",
            true
        )
        wssService.connect()
        return Web3j.build(wssService)
    }
}
