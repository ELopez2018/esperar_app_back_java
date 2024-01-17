package com.example.esperar_app.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * AuthResponse is a class that is used to return the id and access token of a user
 */
@Getter @Setter
public class AuthResponse implements Serializable {
    private Long id;

    private String accessToken;
}
