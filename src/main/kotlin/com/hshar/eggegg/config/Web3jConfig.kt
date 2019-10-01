package com.hshar.eggegg.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.Web3j
import org.web3j.protocol.websocket.EggWebSocketService
import org.web3j.protocol.websocket.WebSocketClient
import java.net.URI
import org.springframework.context.ApplicationEventPublisher

@Configuration
@ConfigurationProperties(prefix = "web3j")
class Web3jConfig {

    @Autowired
    lateinit var applicationEventPublisher: ApplicationEventPublisher

    lateinit var clientUri: String

    @Bean
    fun web3jProvider(): Web3j {

        val wssService = EggWebSocketService(
                WebSocketClient(URI(clientUri)),
                applicationEventPublisher,
                true
        )

        try {
            wssService.connect()
        } catch (e: Throwable) {
            throw RuntimeException("Unable to connect to eth node websocket", e)
        }

        return Web3j.build(wssService)
    }
}
