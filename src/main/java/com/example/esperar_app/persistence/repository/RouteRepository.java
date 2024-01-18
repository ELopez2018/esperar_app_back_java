package com.example.esperar_app.persistence.repository;

import com.example.esperar_app.persistence.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
