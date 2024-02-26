package com.example.esperar_app.persistence.dto.geolocation;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GeolocationResponse {
    private Long vehicleId;
    private String latitude;
    private String longitude;
}
