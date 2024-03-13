package com.example.esperar_app.websocket.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RouteWebSocketRoom {
    private Long routeId;

    private Set<WebSocketSession> sessions;
}
