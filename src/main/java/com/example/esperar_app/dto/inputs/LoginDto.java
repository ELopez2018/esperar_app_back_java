package com.example.esperar_app.dto.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class LoginDto implements Serializable {
    @NotNull @NotBlank
    private String plate;

    @NotNull @NotBlank
    private String password;

    // TODO: Change to plates
    private String username;
}
