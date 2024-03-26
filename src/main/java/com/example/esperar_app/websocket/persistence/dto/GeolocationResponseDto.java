package com.example.esperar_app.websocket.persistence.dto;

import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GeolocationResponseDto {
    private GetRouteDto route;

    private GetVehicleDto vehicle;
}
