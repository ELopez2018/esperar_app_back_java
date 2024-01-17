package com.example.esperar_app.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AuthResponse is a class that is used to return the id and access token of a user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse implements Serializable {
    private Long id;

    private String accessToken;
}
