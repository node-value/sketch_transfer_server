package node_value.project.sketch_transfer_server.web_sockets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

import node_value.project.sketch_transfer_server.dto.MessageDTO;
import node_value.project.sketch_transfer_server.service.JwtService;

import java.util.Collections;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;

public class ServerWebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerWebSocketHandler.class);
    
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    @Autowired JwtService jwtService;

    private String extractUsername(WebSocketSession session) {
        return jwtService.extractUsername(session.getHandshakeHeaders().getFirst("cookie").split("=")[1]);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("Server connection opened");
        logger.info("Connected user: " + extractUsername(session));
        //sessions.put(session.getHandshakeHeaders().getFirst("cookie").split("=")[1], session);
        
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