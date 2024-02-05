package com.example.esperar_app.persistence.dto.user;

import com.example.esperar_app.persistence.utils.DocumentType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class RegisteredUser implements Serializable {
    private Long id;

    private String username;

    private String email;

    private String fullName;

    private String accessToken;

    private String role;

    private String phone;

    private String documentNumber;

    private DocumentType documentType;

    private String gender;

    private String birthdate;
}