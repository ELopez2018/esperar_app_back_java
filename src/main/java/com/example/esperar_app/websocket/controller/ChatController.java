package com.example.esperar_app.websocket.controller;

import com.example.esperar_app.websocket.persistence.entity.ChatMessage;
import com.example.esperar_app.websocket.persistence.entity.ChatNotification;
import com.example.esperar_app.websocket.service.ChatMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS,
        RequestMethod.HEAD,
        RequestMethod.TRACE,
        RequestMethod.PATCH
})
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    private static final Logger logger = LogManager.getLogger(ChatController.class);

    @Autowired
    public ChatController(
            SimpMessagingTemplate messagingTemplate,
            ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/websocket")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        logger.info("Saved websocket message successfully");

        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();

        try {
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipientId(), "/queue/messages",
                    new ChatNotification(
                            randomUUIDString,
                            savedMsg.getSenderId(),
                            savedMsg.getRecipientId(),
                            savedMsg.getContent()
                    )
            );

            logger.info("Sent websocket message successfully");
        } catch (Exception e) {
            logger.error("Failed to send websocket message");
            throw e;
        }
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {
        List<ChatMessage> messages = chatMessageService.findChatMessages(senderId, recipientId);
        return ResponseEntity.ok(messages);
    }

}
