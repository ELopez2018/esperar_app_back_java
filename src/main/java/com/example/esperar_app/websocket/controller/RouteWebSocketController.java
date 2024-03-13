package com.example.esperar_app.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketSession;

@Controller
public class RouteWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public RouteWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/route/{routeId}")
    public void handleWebSocketConnect(WebSocketSession session, @PathVariable Long routeId) {
//        RouteWebSocketRoom room = rooms.get(routeId);

//        room.getSessions().add(session);
    }
}
