package com.example.esperar_app.persistence.dto.user;

import com.example.esperar_app.persistence.utils.DocumentType;
import com.example.esperar_app.persistence.utils.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @Email
    private String email;

    @Size(min = 3, max = 50)
    private String username;

    private String nit;

    private String address;

    private String city;

    private String department;

    private String country;

    private String cellPhone;

    private String whatsapp;

    @Size(min = 8, max = 50)
    private String password;

    @Size(min = 8, max = 50)
    private String confirmedPassword;

    @Size(min = 3, max = 50)
    private String firstName;

    @Size(min = 3, max = 50)
    private String secondName;

    @Size(min = 3, max = 50)
    private String lastName;

    private String birthdate;

    private Gender gender;

    private Long documentNumber;

    private DocumentType documentType;

    private String phone;

    private String licenseExpirationDate;

    public String getLicenseExpirationDate() {
        if(licenseExpirationDate == null) return null;
        return licenseExpirationDate;
    }
}

