package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, Long> {

}
