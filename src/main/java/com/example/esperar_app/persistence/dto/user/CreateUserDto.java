package com.example.esperar_app.persistence.dto.user;

import com.example.esperar_app.persistence.utils.DocumentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class CreateUserDto implements Serializable {
    @Email
    private String email;

    @Size(min = 4, max = 50)
    private String username;

    @Size(min = 8, max = 50)
    private String password;

    @Size(min = 8, max = 50)
    private String confirmPassword;

    @Size(min = 4, max = 50)
    private String firstName;

    @Size(min = 3, max = 50)
    private String secondName;

    @Size(min = 3, max = 50)
    private String lastName;

    private String birthdate;

    private String gender;

    private Long documentNumber;

    private DocumentType documentType;

    private String phone;

    @NotNull
    private Boolean termsAndConditions;
}
