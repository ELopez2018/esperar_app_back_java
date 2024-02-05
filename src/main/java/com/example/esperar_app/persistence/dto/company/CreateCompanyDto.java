package com.example.esperar_app.persistence.dto.inputs.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CreateCompanyDto {
    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty @Email
    private String email;

    @NotEmpty
    private String website;
}
