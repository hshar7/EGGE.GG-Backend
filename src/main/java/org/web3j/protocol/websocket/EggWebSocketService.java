package org.web3j.protocol.websocket;

import lombok.Data;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.context.ApplicationEventPublisher;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;

import java.io.IOException;

import static java.lang.Thread.sleep;

@Data
public class EggWebSocketService extends WebSocketService {

    private WebSocketClient webSocketClient;

    private ApplicationEventPublisher applicationEventPublisher;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(EggWebSocketService.class);

    public EggWebSocketService(WebSocketClient webSocketClient,
                               ApplicationEventPublisher applicationEventPublisher,
                               boolean includeRawResponses) {
        super(webSocketClient, includeRawResponses);
        this.applicationEventPublisher = applicationEventPublisher;
        this.webSocketClient = webSocketClient;
    }

    @Override
    void onWebSocketClose() {
        try {
            super.onWebSocketClose();
        } catch (Throwable t) {
            log.warn("Error when closing websocket, this is expected during a websocket reconnection (for now).", t);
        }
    }

    @Override
    synchronized public <T extends Response> T send(Request request, Class<T> responseType) throws IOException {
        try {
            return super.send(request, responseType);
        } catch (WebsocketNotConnectedException e) {
            log.error("WebSocket disconnected. Reconnecting...");
            try {
                if (this.webSocketClient.reconnectBlocking()) {
                    applicationEventPublisher.publishEvent(new WebsocketDisconnectedEvent(this));
                }
            } catch (InterruptedException error) {
                log.error(e.getLocalizedMessage());
            }
            return null;
        }
    }
}
