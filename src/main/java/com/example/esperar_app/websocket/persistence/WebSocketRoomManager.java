package com.example.esperar_app.websocket.persistence;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Component
public class WebSocketRoomManager {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    public void join(WebSocketSession session) {
        System.out.println("A customer has joined: " + session.getId());
        sessions.add(session);
    }

    public void leave(WebSocketSession session) {
        System.out.println("A customer has leave: " + session.getId());
        sessions.remove(session);
    }

    public void broadcast(String message) {
        sessions.forEach(session -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                System.out.println("Error to send message to " + session.getId());
                throw new RuntimeException(e);
            }
        });
    }

//    public void join(WebSocketSession session, Long routeId) {
//        RouteWebSocketRoom room = getOrCreateRoomForRoute(routeId);
//        room.join(session);
//    }

//    private RouteWebSocketRoom getOrCreateRoomForRoute(Long routeId) {
//       return routeRooms.computeIfAbsent(routeId, id -> new RouteWebSocketRoom());
//    }
}
