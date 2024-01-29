package com.example.esperar_app.persistence.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class GetVehicleDto {
    private Long id;
    private String model;
    private String secondaryPlate;
    private String licensePlate;
}
