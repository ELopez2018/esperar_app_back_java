package com.example.esperar_app.persistence.dto.inputs.user;

import com.example.esperar_app.persistence.utils.DocumentType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class CreateUserDto implements Serializable {
    @NotNull @Email
    private String email;

    @NotNull @Size(min = 4, max = 50)
    private String username;

    @NotNull
    @Size(min = 8, max = 50)
    private String password;

    @NotNull
    @Size(min = 8, max = 50)
    private String confirmPassword;

    @NotNull
    @Size(min = 4, max = 50)
    private String firstName;

    @Size(min = 3, max = 50)
    private String secondName;

    @NotNull
    @Size(min = 3, max = 50)
    private String lastName;

    @NotNull
    private String birthdate;

    @NotNull
    private String gender;

    @NotNull
    private Long documentNumber;

    @NotNull
    private DocumentType documentType;

    @NotNull
    private String phone;
}
