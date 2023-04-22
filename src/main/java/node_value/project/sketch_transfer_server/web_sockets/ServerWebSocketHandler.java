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

import com.fasterxml.jackson.databind.ObjectMapper;

import node_value.project.sketch_transfer_server.dto.ProjectDataDTO;
import node_value.project.sketch_transfer_server.service.JwtService;
import node_value.project.sketch_transfer_server.util.ProjectDataMsgType;

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
        super.supportsPartialMessages();
        logger.info("Server connection opened");
        logger.info("Connected user: " + extractUsername(session));
        
        sessions.put(extractUsername(session), session);
        
        TextMessage message = new TextMessage("User with name \"" + extractUsername(session) + "\" connected to server");

        logger.info("Server sends: {}", message);
        session.sendMessage(message);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Server connection closed: {}", status);
        sessions.remove(extractUsername(session));
    }
        
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();
        logger.info("Server received an incomining message of size " + request.length());
        
        ProjectDataDTO data = new ObjectMapper().readValue(request, ProjectDataDTO.class);
        
        if (data.getType() == ProjectDataMsgType.CHECK.ordinal()) {
            if (sessions.containsKey(data.getReceiver())) {
                data.setType(ProjectDataMsgType.INITIAL.ordinal());
                data.setData("GET_INITIAL");
                sessions.get(data.getReceiver()).sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
                logger.info("Server send to {} this message size {}", data.getReceiver(), data.getData().length());

                data.setType(ProjectDataMsgType.CHECK.ordinal());
                data.setData("OK");
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
                logger.info("Server send to {} this message size {}", data.getReceiver(), data.getData().length());
            } else {
                data.setData("FAILED");
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
            }
        } else if (data.getType() == ProjectDataMsgType.INITIAL.ordinal()) {
            if (sessions.containsKey(data.getReceiver())) {
                sessions.get(data.getReceiver()).sendMessage(new TextMessage(request));
                logger.info("Server received message with initial data");
                //logger.info("Server sent: {}", request);
            } else {
                logger.info(data.getReceiver() + " does not connected to the server");
            }
        }


        //sessions.get(data.getReceiver()).sendMessage(new TextMessage("Message from user " +" messageDTO.getSender()" + ": " + data.getMessage())); // new TextMessage(response);
        
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