package com.example.esperar_app.service.coordinate;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.CoordinateMapper;
import com.example.esperar_app.persistence.dto.inputs.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.inputs.coordinate.UpdateCoordinateDto;
import com.example.esperar_app.persistence.entity.coordinate.Coordinate;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.repository.CoordinateRepository;
import com.example.esperar_app.persistence.repository.RouteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class CoordinateServiceImpl implements CoordinateService {

    private final CoordinateRepository coordinateRepository;
    private final CoordinateMapper coordinateMapper;
    private final RouteRepository routeRepository;

    @Autowired
    public CoordinateServiceImpl(
            CoordinateRepository coordinateRepository,
            CoordinateMapper coordinateMapper,
            RouteRepository routeRepository) {
        this.coordinateRepository = coordinateRepository;
        this.coordinateMapper = coordinateMapper;
        this.routeRepository = routeRepository;
    }

    @Override
    public Coordinate create(CreateCoordinateDto createCoordinateDto) {
        Coordinate coordinate = coordinateMapper.toEntity(createCoordinateDto);
        coordinate.setCreatedAt(Date.valueOf(LocalDate.now()));
        Route route = routeRepository
                .findById(Long.parseLong(createCoordinateDto.getRouteId()))
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

        coordinate.setRoute(route);
        return coordinateRepository.save(coordinate);
    }

    @Override
    public Page<Coordinate> findAll(Pageable pageable) {
        return coordinateRepository.findAll(pageable);
    }

    @Override
    public Coordinate findById(Long id) {
        return coordinateRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Coordinate not found"));
    }

    @Override
    public Coordinate update(Long id, UpdateCoordinateDto updateCoordinateDto) {
        Coordinate coordinate = findById(id);
        BeanUtils.copyProperties(updateCoordinateDto, coordinate, getStrings(updateCoordinateDto));

        coordinate.setUpdatedAt(Date.valueOf(LocalDate.now()));
        return coordinateRepository.save(coordinate);
    }

    @Override
    public void delete(Long id) {
        Coordinate coordinate = findById(id);
        coordinateRepository.delete(coordinate);
    }
}
