package com.example.esperar_app.persistence.dto.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateCompanyDto {
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
