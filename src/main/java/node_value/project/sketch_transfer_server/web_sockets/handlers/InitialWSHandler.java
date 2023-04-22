package node_value.project.sketch_transfer_server.web_sockets.handlers;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

import node_value.project.sketch_transfer_server.dto.ProjectDataDTO;

public class InitialWSHandler extends AbstractWsHandler {
     
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String request = message.getPayload();
        
        ProjectDataDTO data = new ObjectMapper().readValue(request, ProjectDataDTO.class);
        
        if (sessions.containsKey(data.getReceiver())) {
            sessions.get(data.getReceiver()).sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
        } else {
            data.setData("FAILED");
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(data)));
        }
    }
}