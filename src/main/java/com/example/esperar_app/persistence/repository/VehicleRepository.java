package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findBySoatExpirationDateBetweenOrTecnoMechanicalExpirationDateBetween(
            Date soatExpirationDate,
            Date soatExpirationDate2,
            Date tecnoMechanicalExpirationDate,
            Date tecnoMechanicalExpirationDate2
    );

    @Query("SELECT v FROM Vehicle v " +
            "WHERE MONTH(v.soatExpirationDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(v.soatExpirationDate) = YEAR(CURRENT_DATE)")
    Page<Vehicle> findVehiclesWithSoatSoonToExpire(Pageable pageable);

    @Query("SELECT v FROM Vehicle v " +
            "WHERE MONTH(v.tecnoMechanicalExpirationDate) = MONTH(CURRENT_DATE) " +
            "AND YEAR(v.tecnoMechanicalExpirationDate) = YEAR(CURRENT_DATE)")
    Page<Vehicle> findVehiclesWithTechnoMechanicalSoonToExpire(Pageable pageable);

    @Query("SELECT v FROM Vehicle v " +
            "WHERE v.route.id = :routeId")
    Page<Vehicle> findVehiclesByRouteId(Pageable pageable, Long routeId);

    @Query("SELECT v FROM Vehicle v " +
            "WHERE v.owner.id = :id AND (v.owner.role.name = 'CEO' OR v.owner.role.name = 'ADMINISTRATOR')")
    Page<Vehicle> findVehiclesByOwnerId(Pageable pageable, Long id);

}
