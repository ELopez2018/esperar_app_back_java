package com.example.esperar_app.persistence.dto.geolocation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AddGeolocationDto {
    private Long vehicleId;

    private String latitude;

    private String longitude;
}
