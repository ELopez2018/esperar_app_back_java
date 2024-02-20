package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findBySoatExpirationDateBetweenOrTecnoMechanicalExpirationDateBetween(
            Date soatExpirationDate,
            Date soatExpirationDate2,
            Date tecnoMechanicalExpirationDate,
            Date tecnoMechanicalExpirationDate2
    );
}
