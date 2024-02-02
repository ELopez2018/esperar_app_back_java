package com.example.esperar_app.service.chat;

import com.example.esperar_app.persistence.entity.chat.TempChatMessage;
import com.example.esperar_app.persistence.repository.chat.TempChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TempChatMessageService {

    private final TempChatMessageRepository tempChatMessageRepository;
    private final ChatRoomService chatRoomService;

    @Autowired
    public TempChatMessageService(
            TempChatMessageRepository tempChatMessageRepository,
            ChatRoomService chatRoomService) {
        this.tempChatMessageRepository = tempChatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    public TempChatMessage save(TempChatMessage tempChatMessage) {
        String senderId = tempChatMessage.getSenderId();
        String recipientId = tempChatMessage.getRecipientId();

        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                true)
                .orElseThrow(null);

        tempChatMessage.setChatId(chatId);
        return tempChatMessageRepository.save(tempChatMessage);
    }

}
