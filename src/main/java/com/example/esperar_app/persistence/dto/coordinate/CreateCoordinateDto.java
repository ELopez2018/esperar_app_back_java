package com.example.esperar_app.persistence.dto.coordinate;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateCoordinateDto {
    @NotEmpty
    private String latitude;

    @NotEmpty
    private String longitude;

    @NotEmpty
    private String routeId;
}
