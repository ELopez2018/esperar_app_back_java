package com.example.esperar_app.service.route;

import com.example.esperar_app.persistence.dto.inputs.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.inputs.route.UpdateRouteDto;
import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RouteService {
    GetRouteDto create(CreateRouteDto createRouteDto);

    Page<GetRouteDto> findAll(Pageable pageable);

    GetRouteDto findById(Long id);

    GetRouteDto update(Long id, UpdateRouteDto updateRouteDto);

    void delete(Long id);
}
