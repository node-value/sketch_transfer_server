package node_value.project.sketch_transfer_server.web_sockets.handlers;

import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import node_value.project.sketch_transfer_server.dto.ProjectDataDTO;

public class DeleteWSHandler extends AbstractWsHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(DeleteWSHandler.class);
    
    protected final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();       
        ProjectDataDTO data = mapper.readValue(message.getPayload(), ProjectDataDTO.class);
        
        if (sessions.containsKey(data.getReceiver())) {
            sessions.get(data.getReceiver()).sendMessage(new TextMessage(mapper.writeValueAsString(data)));
        } else {
            data.setData("FAILED");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(data)));
        }
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
        logger.info("Server transport error: {} {}", exception.getMessage(), exception.getCause());
    }
}
