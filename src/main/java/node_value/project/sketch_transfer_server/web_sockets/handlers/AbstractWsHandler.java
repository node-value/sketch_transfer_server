package node_value.project.sketch_transfer_server.web_sockets.handlers;

import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import node_value.project.sketch_transfer_server.service.JwtService;

import java.util.Collections;
import java.util.List;

public abstract class AbstractWsHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractWsHandler.class);

    @Autowired JwtService jwtService;

    protected String extractUsername(WebSocketSession session) {
        return jwtService.extractUsername(session.getHandshakeHeaders().getFirst("cookie").split("=")[1]);
    }
        
    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }

    @Override
    public void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        logger.info("Received pong message from {} from addr {}", extractUsername(session), session.getRemoteAddress().getAddress().getHostAddress());        
    }
}
