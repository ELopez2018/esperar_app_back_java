package com.example.esperar_app.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Este es el punto de entrada para el cliente, ejemplo "ws://yourdomain/portfolio"
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:5173").withSockJS();
        registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:5173");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefijo para los destinos de aplicación, donde los clientes envían mensajes
        config.setApplicationDestinationPrefixes("/app");
        /*
         Destinos de salida, donde el servidor envía mensajes a los clientes.
         /topic se utiliza para enviar mensajes a todos los suscriptores
         /queue para enviar mensajes a suscriptores específicos.
         */
        config.enableSimpleBroker("/topic", "/queue");
    }
}