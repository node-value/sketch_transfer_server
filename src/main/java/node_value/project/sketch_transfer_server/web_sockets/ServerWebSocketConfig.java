package node_value.project.sketch_transfer_server.web_sockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import node_value.project.sketch_transfer_server.web_sockets.handlers.*;

@Configuration @EnableWebSocket
public class ServerWebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(CheckWSHandler(),   "/ws_project/check"  )
                .addHandler(InitialWSHandler(), "/ws_project/initial")
                .addHandler(DeleteWSHandler(),  "/ws_project/delete" )
                .addHandler(AddWSHandler(),     "/ws_project/add"    )
                .addHandler(MoveWSHandler(),    "/ws_project/move"   )
                .addHandler(ChatWSHandler(),    "/ws_project/chat"   );
    }
    
    @Bean WebSocketHandler CheckWSHandler()   { return new CheckWSHandler  (); }
    @Bean WebSocketHandler InitialWSHandler() { return new InitialWSHandler(); }
    @Bean WebSocketHandler DeleteWSHandler()  { return new DeleteWSHandler (); }
    @Bean WebSocketHandler AddWSHandler()     { return new AddWSHandler    (); }
    @Bean WebSocketHandler MoveWSHandler()    { return new MoveWSHandler   (); }
    @Bean WebSocketHandler ChatWSHandler()    { return new ChatWSHandler   (); }

    @Bean ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024*1024*5);
        return container;
    }
}