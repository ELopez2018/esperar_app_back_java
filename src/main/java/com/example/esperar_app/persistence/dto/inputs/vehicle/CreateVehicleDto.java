package com.example.esperar_app.persistence.dto.inputs.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateVehicleDto {
    @NotNull
    private String licensePlate;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    private String color;

    @NotNull
    private Integer year;

    @NotNull
    private Double cylinderCapacity;

    @NotNull
    private Integer capacity;

    @NotNull
    private Integer occupancy;

    @NotNull
    private Long driverId;

    @NotNull
    private Long companyId;
}
