package com.example.esperar_app.persistence.dto.coordinate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class GetCoordinateDto {
    private Long id;

    private String latitude;

    private String longitude;

    private String createdAt;

    private String updatedAt;

    private String deletedAt;
}
