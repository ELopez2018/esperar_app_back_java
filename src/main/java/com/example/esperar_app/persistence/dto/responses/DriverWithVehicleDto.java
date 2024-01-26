package com.example.esperar_app.persistence.dto.responses;

import com.example.esperar_app.persistence.dto.inputs.vehicle.GetVehicle;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DriverWithVehicleDto {
    private GetUser userData;

    private GetVehicle vehicleData;
}
