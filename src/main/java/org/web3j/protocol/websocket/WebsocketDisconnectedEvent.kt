package org.web3j.protocol.websocket

import org.springframework.context.ApplicationEvent

class WebsocketDisconnectedEvent(source: Any) : ApplicationEvent(source)
