package com.example.esperar_app.controller;

import com.example.esperar_app.persistence.dto.inputs.chat.CreateChatMessageDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    public CreateChatMessageDto register(
            @Payload CreateChatMessageDto chatMessage,
            SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getUser());
        return chatMessage;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public CreateChatMessageDto sendMessage(@Payload CreateChatMessageDto chatMessage) {
        System.out.println(chatMessage);
        return chatMessage;
    }
}
