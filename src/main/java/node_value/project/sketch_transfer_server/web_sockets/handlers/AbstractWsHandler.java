package node_value.project.sketch_transfer_server.web_sockets.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;

import node_value.project.sketch_transfer_server.service.JwtService;

import java.util.Collections;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractWsHandler extends TextWebSocketHandler implements SubProtocolCapable {

    private static final Logger logger = LoggerFactory.getLogger(CheckWSHandler.class);
    
    protected final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    @Autowired JwtService jwtService;

    protected String extractUsername(WebSocketSession session) {
        return jwtService.extractUsername(session.getHandshakeHeaders().getFirst("cookie").split("=")[1]);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.supportsPartialMessages();
        logger.info("Server connection opened on uri " + session.getUri().getPath()); 
        sessions.put(extractUsername(session), session);
        session.sendMessage(new TextMessage("User with name \"" + extractUsername(session) + "\" connected to server"));
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Server connection closed: {}", status);
        sessions.remove(extractUsername(session));
    }
        
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.info("Server transport error: {}", exception.getMessage());
    }
    
    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }
}
