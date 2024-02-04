package com.example.esperar_app.service.route;

import com.example.esperar_app.persistence.dto.inputs.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.inputs.route.UpdateRouteDto;
import com.example.esperar_app.persistence.entity.route.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RouteService {
    Route create(CreateRouteDto createRouteDto);

    Page<Route> findAll(Pageable pageable);

    Route findById(Long id);

    Route update(Long id, UpdateRouteDto updateRouteDto);

    void delete(Long id);
}
