package com.example.esperar_app.service.coordinate;

import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.UpdateCoordinateDto;
import com.example.esperar_app.persistence.entity.coordinate.Coordinate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CoordinateService {

    GetCoordinateDto create(CreateCoordinateDto createCoordinateDto);

    Page<GetCoordinateDto> findAll(Pageable pageable);

    GetCoordinateDto findById(Long id);

    GetCoordinateDto update(Long id, UpdateCoordinateDto updateCoordinateDto);

    void delete(Long id);

    List<Coordinate> createAll(List<CoordinateDto> coordinates, Long id);
}
