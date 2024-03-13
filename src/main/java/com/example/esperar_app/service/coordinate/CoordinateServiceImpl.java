package com.example.esperar_app.service.coordinate;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.CoordinateMapper;
import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.UpdateCoordinateDto;
import com.example.esperar_app.persistence.entity.coordinate.Coordinate;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.station.Station;
import com.example.esperar_app.persistence.repository.CoordinateRepository;
import com.example.esperar_app.persistence.repository.RouteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class CoordinateServiceImpl implements CoordinateService {

    private final CoordinateRepository coordinateRepository;
    private final CoordinateMapper coordinateMapper;
    private final RouteRepository routeRepository;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public CoordinateServiceImpl(
            CoordinateRepository coordinateRepository,
            CoordinateMapper coordinateMapper,
            RouteRepository routeRepository) {
        this.coordinateRepository = coordinateRepository;
        this.coordinateMapper = coordinateMapper;
        this.routeRepository = routeRepository;
    }

    /**
     * Create a coordinate
     * @param createCoordinateDto - coordinate data to create
     * @return created coordinate
     */
    @Override
    public GetCoordinateDto create(CreateCoordinateDto createCoordinateDto) {
        Coordinate coordinate = coordinateMapper.toEntity(createCoordinateDto);

        Route route = routeRepository
                .findById(Long.parseLong(createCoordinateDto.getRouteId()))
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

        coordinate.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        coordinate.setRoute(route);

        try {
            Coordinate coordinateSaved = coordinateRepository.save(coordinate);
            logger.info("Coordinate created");
            return coordinateMapper.coordinateToGetCoordinateDto(coordinateSaved);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error creating coordinate");
        }
    }

    /**
     * Get all coordinates paginated
     * @param pageable - page information
     * @return paginated coordinates
     */
    @Override
    public Page<GetCoordinateDto> findAll(Pageable pageable) {
        Page<Coordinate> coordinates = coordinateRepository.findAll(pageable);
        logger.info("Returning all coordinates paginated");
        return coordinates.map(coordinateMapper::coordinateToGetCoordinateDto);
    }

    /**
     * Get a coordinate by id
     * @param id - coordinate identifier
     * @return coordinate found
     */
    @Override
    public GetCoordinateDto findById(Long id) {
        Coordinate coordinateFound = coordinateRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Coordinate not found"));

        logger.info("Returning coordinate found");
        return coordinateMapper.coordinateToGetCoordinateDto(coordinateFound);
    }

    /**
     * Update a coordinate by identifier
     * @param id - coordinate identifier
     * @param updateCoordinateDto - coordinate data to update
     * @return updated coordinate
     */
    @Override
    public GetCoordinateDto update(Long id, UpdateCoordinateDto updateCoordinateDto) {
        Coordinate coordinate = coordinateRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Coordinate not found"));

        BeanUtils.copyProperties(updateCoordinateDto, coordinate, getStrings(updateCoordinateDto));
        coordinate.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        try {
            Coordinate coordinateUpdated = coordinateRepository.save(coordinate);
            logger.info("Coordinate updated");
            return coordinateMapper.coordinateToGetCoordinateDto(coordinateUpdated);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error updating coordinate");
        }

    }

    /**
     * Delete a coordinate by identifier
     * @param id - coordinate identifier
     */
    @Override
    public void delete(Long id) {
        Coordinate coordinate = coordinateRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Coordinate not found"));

        try {
            coordinateRepository.delete(coordinate);
            logger.info("Coordinate deleted");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error deleting coordinate");
        }
    }

    @Override
    public List<Coordinate> createAll(List<CoordinateDto> coordinates, Route route) {
        List<Coordinate> coordinatesSaved = new ArrayList<>();

        for (CoordinateDto coordinateDto : coordinates) {
            Coordinate coordinate = coordinateMapper.coordinateDtoToEntity(coordinateDto);
            coordinate.setRoute(route);
            coordinate.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            coordinatesSaved.add(coordinate);
            coordinateRepository.save(coordinate);
        }

        return coordinatesSaved;
    }

    @Override
    public List<Coordinate> createAll(List<CoordinateDto> coordinates, Station station) {
        List<Coordinate> coordinatesSaved = new ArrayList<>();

        for (CoordinateDto coordinateDto : coordinates) {
            Coordinate coordinate = coordinateMapper.coordinateDtoToEntity(coordinateDto);
            coordinate.setStation(station);
            coordinate.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            coordinatesSaved.add(coordinate);
            coordinateRepository.save(coordinate);
        }

        return coordinatesSaved;
    }
}
