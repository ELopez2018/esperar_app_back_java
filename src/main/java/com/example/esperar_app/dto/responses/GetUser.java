package com.example.esperar_app.dto.responses;

import com.example.esperar_app.persistence.utils.DocumentType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GetUser {
    private Long id;
    private String email;
    private String username;
    private String fullName;
    private String image;
    private String firstName;
    private String secondName;
    private String lastName;
    private String birthdate;
    private String gender;
    private Long documentNumber;
    private DocumentType documentType;
    private String phone;
    private Long currentCountry;
    private String createdAt;
    private String updatedAt;
    private String deletedAt;
}
