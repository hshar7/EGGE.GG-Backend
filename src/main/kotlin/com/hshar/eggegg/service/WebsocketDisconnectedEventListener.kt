package com.hshar.eggegg.service

import com.hshar.eggegg.config.EventListenerManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import org.web3j.protocol.websocket.WebsocketDisconnectedEvent

@Component
class WebsocketDisconnectedEventListener : ApplicationListener<WebsocketDisconnectedEvent> {

    @Autowired
    lateinit var eventListenerManager: EventListenerManager

    override fun onApplicationEvent(event: WebsocketDisconnectedEvent) {
        eventListenerManager.initializeSubscribers()
    }
}
