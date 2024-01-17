package com.example.esperar_app.persistence.repository.chat;

import com.example.esperar_app.persistence.entity.chat.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepository extends JpaRepository<UserChat, Long> {

}
