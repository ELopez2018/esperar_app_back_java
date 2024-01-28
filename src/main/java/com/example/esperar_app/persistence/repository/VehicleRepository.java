package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
