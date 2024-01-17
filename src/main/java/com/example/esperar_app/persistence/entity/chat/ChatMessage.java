package com.example.esperar_app.persistence.entity.chat;

import com.example.esperar_app.persistence.entity.security.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "chat_messages")
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor @RequiredArgsConstructor
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private Chat chat;

    @ManyToMany
    @JoinTable(
            name = "chat_message_receivers",
            joinColumns = @JoinColumn(name = "chat_message_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_id")
    )
    private List<User> receivers;

    @Column(length = 1000)
    private String text;
}
