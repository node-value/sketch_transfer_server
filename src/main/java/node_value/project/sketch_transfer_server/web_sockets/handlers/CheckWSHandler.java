package node_value.project.sketch_transfer_server.web_sockets.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import node_value.project.sketch_transfer_server.dto.ProjectDataDTO;

public class CheckWSHandler extends AbstractWsHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(CheckWSHandler.class);
    
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();
        logger.info("Server received an incomining message of size " + request.length());
        
        ProjectDataDTO data = new ObjectMapper().readValue(request, ProjectDataDTO.class);
        
      
        if (sessions.containsKey(data.getReceiver())) {
            sessions.get(data.getReceiver()).sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
            logger.info("Server send to {} this message size {}", data.getReceiver(), data.getData().length());
        } else {
            data.setData("FAILED");
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
        }
    }
}