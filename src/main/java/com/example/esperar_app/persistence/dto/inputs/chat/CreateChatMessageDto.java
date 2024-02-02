package com.example.esperar_app.persistence.dto.inputs.chat;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CreateChatMessageDto {
    @NotEmpty
    private String sender;

    @NotEmpty
    private String message;
}
