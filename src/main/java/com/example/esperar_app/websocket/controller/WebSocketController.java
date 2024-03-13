package com.example.esperar_app.websocket.controller;

import com.example.esperar_app.websocket.persistence.WebSocketRoomManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Controller
public class WebSocketController extends TextWebSocketHandler {

    private final WebSocketRoomManager webSocketRoomManager;

    @Autowired
    public WebSocketController(WebSocketRoomManager webSocketRoomManager) {
        this.webSocketRoomManager = webSocketRoomManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketRoomManager.join(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketRoomManager.leave(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Manejar mensajes de texto entrantes
        // Aquí podrías implementar la lógica para procesar los mensajes enviados por el vehículo

        // Por ejemplo, si el mensaje es "start" podrías enviar una notificación a los conductores
        // O si el mensaje es "stop" podrías enviar una notificación a los conductores
        // O si el mensaje es "location" podrías enviar la ubicación del vehículo a los conductores
        // O si el mensaje es "route" podrías enviar la ruta del vehículo a los conductores
        // O si el mensaje es "status" podrías enviar el estado del vehículo a los conductores
        // O si el mensaje es "emergency" podrías enviar una notificación de emergencia a los conductores
        // O si el mensaje es "message" podrías enviar un mensaje a los conductores


    }
}
