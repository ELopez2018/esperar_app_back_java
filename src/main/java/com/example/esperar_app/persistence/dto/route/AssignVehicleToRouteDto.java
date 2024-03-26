package com.example.esperar_app.persistence.dto.route;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AssignVehicleToRouteDto {
    private Long routeId;
    private Long[] vehiclesIds;
}
