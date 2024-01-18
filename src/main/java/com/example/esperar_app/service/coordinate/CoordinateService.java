package com.example.esperar_app.service.coordinate;

import com.example.esperar_app.persistence.dto.inputs.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.inputs.coordinate.UpdateCoordinateDto;
import com.example.esperar_app.persistence.entity.Coordinate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoordinateService {

    Coordinate create(CreateCoordinateDto createCoordinateDto);

    Page<Coordinate> findAll(Pageable pageable);

    Coordinate findById(Long id);

    Coordinate update(Long id, UpdateCoordinateDto updateCoordinateDto);

    void delete(Long id);
}
