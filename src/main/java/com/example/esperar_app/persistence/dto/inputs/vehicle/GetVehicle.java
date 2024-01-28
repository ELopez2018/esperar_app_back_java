package com.example.esperar_app.persistence.dto.inputs.vehicle;

import com.example.esperar_app.persistence.dto.responses.GetUser;
import com.example.esperar_app.persistence.entity.security.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @Builder
@AllArgsConstructor @RequiredArgsConstructor
public class GetVehicle {
    private Long id;

    private String licensePlate;

    private String secondaryPlate;

    private String model;

    private String brand;

    private Integer year;

    private String color;

    private Double cylinderCapacity;

    private Integer capacity;

    private Integer occupancy;

    private List<User> drivers;
}
