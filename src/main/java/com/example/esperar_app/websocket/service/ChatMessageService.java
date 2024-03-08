package com.example.esperar_app.websocket.service;

import com.example.esperar_app.websocket.persistence.entity.ChatMessage;
import com.example.esperar_app.websocket.persistence.repository.ChatMessageRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {
    private static final Logger logger = LogManager.getLogger(ChatMessageService.class);

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatMessageService(
            ChatMessageRepository chatMessageRepository,
            ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    /**
     * Save the websocket message to the database
     * @param chatMessage the websocket message to save
     * @return the saved websocket message
     */
    public ChatMessage save(ChatMessage chatMessage) {
        logger.info("Trying to save a new websocket-message");
        String senderId = chatMessage.getSenderId();
        String recipientId = chatMessage.getRecipientId();

        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                true)
                .orElseThrow(null);

        chatMessage.setChatId(chatId);

        try {
            return chatMessageRepository.save(chatMessage);
        } catch (Exception e) {
            logger.error("Failed to save websocket-message", e);
            throw e;
        }
    }

    /**
     * Find all websocket messages between two users
     * @param senderId the sender's id
     * @param recipientId the recipient's id
     * @return a list of websocket messages between the two users
     */
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        logger.info("Trying to find websocket-messages between two users");
        logger.info("Sender ID: " + senderId);
        logger.info("Recipient ID: " + recipientId);

        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false);

        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }

}
