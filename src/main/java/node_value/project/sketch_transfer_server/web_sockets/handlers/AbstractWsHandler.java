package node_value.project.sketch_transfer_server.web_sockets.handlers;

import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;

import node_value.project.sketch_transfer_server.service.JwtService;

import java.util.Collections;
import java.util.List;

public abstract class AbstractWsHandler extends TextWebSocketHandler implements SubProtocolCapable {

    @Autowired JwtService jwtService;

    protected String extractUsername(WebSocketSession session) {
        return jwtService.extractUsername(session.getHandshakeHeaders().getFirst("cookie").split("=")[1]);
    }
        
    @Override
    public List<String> getSubProtocols() {
        return Collections.singletonList("subprotocol.demo.websocket");
    }
}
