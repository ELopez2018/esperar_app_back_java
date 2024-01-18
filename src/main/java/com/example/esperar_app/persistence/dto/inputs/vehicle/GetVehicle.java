package com.example.esperar_app.persistence.dto.inputs.vehicle;

import com.example.esperar_app.persistence.dto.responses.GetUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter @Builder
@AllArgsConstructor @RequiredArgsConstructor
public class GetVehicle {
    private Long id;

    private String licensePlate;

    private String model;

    private String brand;

    private Integer year;

    private String color;

    private Double cylinderCapacity;

    private Integer capacity;

    private Integer occupancy;

    private GetUser owner;

    private GetUser driver;
}
