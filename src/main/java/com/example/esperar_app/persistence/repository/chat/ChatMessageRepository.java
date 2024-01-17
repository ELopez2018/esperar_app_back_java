package com.example.esperar_app.persistence.repository.chat;

import com.example.esperar_app.persistence.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

}
