package com.example.esperar_app.persistence.dto.vehicle;

import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.utils.VehicleStatus;
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
     * The license plate of the vehicle
     */
    private String licensePlate;

    /**
     * The year of the vehicle
     */
    private String secondaryPlate;

    /**
     * The brand of the vehicle
     */
    private String brand;

    /**
     * The brand of the vehicle
     */
    private String model;

    /**
     * The color of the vehicle
     */
    private String color;

    /**
     * The year of the vehicle
     */
    private Integer year;

    /**
     * The cylinder capacity of the vehicle
     */
    private Double cylinderCapacity;

    /**
     * The capacity of the vehicle
     */
    private Integer capacity;

    /**
     * The occupancy of the vehicle
     */
    private Integer occupancy;

    /**
     * The soat expiration date of the vehicle
     */
    private String soatExpirationDate;

    /**
     * The tecnomechanical expiration date of the vehicle
     */
    private String tecnoMechanicalExpirationDate;

    /**
     * The status of the vehicle
     */
    private VehicleStatus status;

    /**
     * The route of the vehicle
     */
    private GetRouteDto route;

    /**
     * Main driver data
     */
    private GetUserDto mainDriver;
}
