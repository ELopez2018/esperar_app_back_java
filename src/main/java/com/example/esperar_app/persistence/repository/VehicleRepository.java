package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    @Query("SELECT v FROM Vehicle v JOIN FETCH v.driver d JOIN v.company c WHERE c.id = :companyId")
    List<Vehicle> findVehiclesWithDriverByCompanyId(@Param("companyId") Long companyId);

}
