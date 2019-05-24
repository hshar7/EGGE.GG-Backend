package org.web3j.protocol.websocket;

import org.springframework.context.ApplicationEvent;

public class WebsocketDisconnectedEvent extends ApplicationEvent {
    WebsocketDisconnectedEvent(Object source) {
        super(source);
    }
}
