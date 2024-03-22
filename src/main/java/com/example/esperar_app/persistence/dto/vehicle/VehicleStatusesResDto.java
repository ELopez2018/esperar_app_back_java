package com.example.esperar_app.persistence.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class VehicleStatusesResDto {
    private List<String> vehicleStatuses;
}
