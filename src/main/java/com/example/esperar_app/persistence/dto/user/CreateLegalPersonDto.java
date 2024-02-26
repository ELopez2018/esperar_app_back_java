package com.example.esperar_app.persistence.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object to create a legal person
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CreateLegalPersonDto {
    /**
     * Name of the company
     */
    @NotEmpty
    private String name;

    /**
     * NIT of the company
     */
    @NotEmpty
    private String nit;

    /**
     * Legal representative of the company
     */
    @Positive
    private Long legalRepresentativeId;

    /**
     * Address of the company
     */
    private String address;

    /**
     * Email of the company
     */
    @Email
    private String email;

    /**
     * Neighborhood of the company
     */
    private String neighborhood;

    /**
     * City of the company
     */
    private String city;

    /**
     * Department of the company
     */
    private String department;

    /**
     * Country of the company
     */
    private String country;

    /**
     * Phone of the company
     */
    private String phone;

    /**
     * Cell phone of the company
     */
    private String cellPhone;

    /**
     * WhatsApp of the company
     */
    private String whatsapp;

    /**
     * Username of the company
     */
    @Size(min = 4, max = 50)
    private String username;

    /**
     * Password of the company
     */
    @Size(min = 8, max = 50)
    private String password;

    /**
     * Confirm password of the company
     */
    @Size(min = 8, max = 50)
    private String confirmPassword;

    /**
     * If accept the terms and conditions
     */
    @NotNull
    private Boolean termsAndConditions;
}
