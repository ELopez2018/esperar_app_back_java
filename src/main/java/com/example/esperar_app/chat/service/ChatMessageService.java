package com.example.esperar_app.chat.service;

import com.example.esperar_app.chat.persistence.entity.ChatMessage;
import com.example.esperar_app.chat.persistence.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    @Autowired
    public ChatMessageService(
            ChatMessageRepository chatMessageRepository,
            ChatRoomService chatRoomService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        String senderId = chatMessage.getSenderId();
        String recipientId = chatMessage.getRecipientId();

        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                true)
                .orElseThrow(null);

        chatMessage.setChatId(chatId);
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false);

        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }

}
