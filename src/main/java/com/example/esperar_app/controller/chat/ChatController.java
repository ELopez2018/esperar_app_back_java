package com.example.esperar_app.controller.chat;

import com.example.esperar_app.persistence.dto.inputs.chat.CreateChatMessageDto;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/chat/{recipient}")
    @SendTo("/user/{recipient}/queue/chat")
    public CreateChatMessageDto sendPrivateMessage(
            @DestinationVariable String recipient, CreateChatMessageDto message) {
        System.out.println("ESTAMOS EN CHAT PRIVADO");
        System.out.println(message.getSender() + " dice: " + message.getMessage());
        System.out.println("El destinatario es: " + recipient);
        return message;
    }

    @MessageMapping("/chat/public")
    @SendTo("/topic/public")
    public CreateChatMessageDto sendPublicMessage(CreateChatMessageDto message) {
        System.out.println("ESTAMOS EN EL CHAT PÚBLICO");
        System.out.println(message.getSender() + " dice: " + message.getMessage());
        return message;
    }
}

