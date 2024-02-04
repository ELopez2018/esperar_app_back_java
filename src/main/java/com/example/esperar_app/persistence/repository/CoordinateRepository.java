package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.coordinate.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {

}
