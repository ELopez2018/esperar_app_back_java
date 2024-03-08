package com.example.esperar_app.websocket.persistence.repository;

import com.example.esperar_app.websocket.persistence.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId);
}
