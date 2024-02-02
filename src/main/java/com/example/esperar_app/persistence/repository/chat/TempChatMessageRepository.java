package com.example.esperar_app.persistence.repository.chat;

import com.example.esperar_app.persistence.entity.chat.TempChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TempChatMessageRepository extends JpaRepository<TempChatMessage, String> {

}