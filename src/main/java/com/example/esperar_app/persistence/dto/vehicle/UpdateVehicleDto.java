package com.example.esperar_app.persistence.dto.vehicle;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateVehicleDto {
    private String licensePlate;

    private String model;

    private String brand;

    private Integer year;

    private String color;

    private Double cylinderCapacity;

    private Integer capacity;

    private Integer occupancy;

    private Long driverId;

    private String soatExpirationDate;

    private String tecnoMechanicalExpirationDate;
}
