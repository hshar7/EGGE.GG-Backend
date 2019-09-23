package com.hshar.eggegg.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.websocket.EggWebSocketService
import org.web3j.protocol.websocket.WebSocketClient
import java.net.ConnectException
import java.net.URI
import org.springframework.context.ApplicationEventPublisher

@Configuration
class Web3jConfig {

    @Autowired
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    @Bean
    fun web3jProvider(): Web3j {

        val wssService = EggWebSocketService(
                WebSocketClient(URI("wss://mainnet.infura.io/ws/v3/1703c01ec0814e7796155ff061b350a1")),
                applicationEventPublisher,
                true
        )

        try {
            wssService.connect()
        } catch (e: ConnectException) {
            throw RuntimeException("Unable to connect to eth node websocket", e)
        }

        return Web3j.build(wssService)
    }
}
