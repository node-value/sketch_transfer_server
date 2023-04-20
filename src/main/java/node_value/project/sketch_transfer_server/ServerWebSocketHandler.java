package node_value.project.sketch_transfer_server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;

public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);
    
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection opened");
        
        sessions.put(session.getHandshakeHeaders().getFirst("cookie").split("=")[1], session);
        
        TextMessage message = new TextMessage("one-time message from server ");

        logger.info("Server sends: {}", message);
        session.sendMessage(message);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Server connection closed: {}", status);
        sessions.remove(session.getHandshakeHeaders().getFirst("username"));
    }
        
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();
        logger.info("Server received: {}", request);
        
        MessageDTO messageDTO = new ObjectMapper().readValue(request, MessageDTO.class);
        
        String response = String.format("Broadcast to all connected clients from server to '%s'", HtmlUtils.htmlEscape(request));
        logger.info("Server sends: {}", response);

        
        sessions.get(messageDTO.getReceiver()).sendMessage(new TextMessage("Message from user " + messageDTO.getSender() + ": " + messageDTO.getMessage())); // new TextMessage(response);
        
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