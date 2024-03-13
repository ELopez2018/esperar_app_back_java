package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.station.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StationRepository extends JpaRepository<Station, Long> {

    @Query("SELECT s FROM Station s JOIN s.routes r WHERE r.id = :id")
    Page<Station> findAllByRouteId(Long id, Pageable pageable);
}
