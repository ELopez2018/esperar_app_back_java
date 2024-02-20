package com.example.esperar_app.persistence.dto.vehicle;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CreateVehicleDto {
    @NotNull
    private String licensePlate;

    @NotNull
    private String secondaryPlate;

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

    private String soatExpirationDate;

    private String tecnoMechanicalExpirationDate;

    public String getSoatExpirationDate() {
        if(soatExpirationDate == null) return null;
        return soatExpirationDate;
    }

    public String getTecnoMechanicalExpirationDate() {
        if(tecnoMechanicalExpirationDate == null) return null;
        return tecnoMechanicalExpirationDate;
    }
}
