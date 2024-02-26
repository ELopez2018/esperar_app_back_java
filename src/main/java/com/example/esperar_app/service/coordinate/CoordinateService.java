package com.example.esperar_app.service.coordinate;

import com.example.esperar_app.persistence.dto.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.UpdateCoordinateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoordinateService {

    GetCoordinateDto create(CreateCoordinateDto createCoordinateDto);

    Page<GetCoordinateDto> findAll(Pageable pageable);

    GetCoordinateDto findById(Long id);

    GetCoordinateDto update(Long id, UpdateCoordinateDto updateCoordinateDto);

    void delete(Long id);
}
