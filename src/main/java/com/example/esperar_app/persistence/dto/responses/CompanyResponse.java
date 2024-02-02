package com.example.esperar_app.persistence.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CompanyResponse {
    private Long id;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;

    private String website;

    private Long ceoId;

    private List<Long> vehiclesIds;
}
