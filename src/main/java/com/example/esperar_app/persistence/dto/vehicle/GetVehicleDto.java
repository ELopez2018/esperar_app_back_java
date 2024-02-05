package com.example.esperar_app.persistence.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object for the vehicle entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetVehicleDto {
    /**
     * The id of the vehicle
     */
    private Long id;

    /**
     * The brand of the vehicle
     */
    private String model;

    /**
     * The year of the vehicle
     */
    private String secondaryPlate;

    /**
     * The license plate of the vehicle
     */
    private String licensePlate;
}
