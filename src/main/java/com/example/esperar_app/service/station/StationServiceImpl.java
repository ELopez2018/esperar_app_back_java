package com.example.esperar_app.service.station;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.StationMapper;
import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import com.example.esperar_app.persistence.dto.station.CreateStationDto;
import com.example.esperar_app.persistence.dto.station.GetStationDto;
import com.example.esperar_app.persistence.entity.coordinate.Coordinate;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.station.Station;
import com.example.esperar_app.persistence.repository.RouteRepository;
import com.example.esperar_app.persistence.repository.StationRepository;
import com.example.esperar_app.service.coordinate.CoordinateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;

    private final StationMapper stationMapper;

    private final RouteRepository routeRepository;

    private final CoordinateService coordinateService;

    @Autowired
    public StationServiceImpl(
            StationRepository stationRepository,
            StationMapper stationMapper,
            RouteRepository routeRepository,
            CoordinateService coordinateService) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
        this.routeRepository = routeRepository;
        this.coordinateService = coordinateService;
    }

    @Override
    public List<Station> createAll(List<CreateStationDto> stations, Route route) {
        List<Station> stationSaved = new ArrayList<>();

        System.out.println("Trying create stations in route: " + route.getId());

        for(CreateStationDto stationDto : stations) {
            Station station = stationMapper.createStationDtoToStation(stationDto);
            station.getRoutes().add(route);
            station.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            try {
                stationSaved.add(stationRepository.save(station));
            } catch (Exception e) {
                System.out.println("Error creating station: " + e.getMessage());
            }
        }

        return stationSaved;
    }

    @Override
    public GetStationDto create(CreateStationDto createStationDto) {
        Route routeFound = routeRepository
                .findById(createStationDto.getRouteId())
                .orElseThrow(() -> new ObjectNotFoundException("Route not found."));

        Station station = stationMapper.createStationDtoToStation(createStationDto);

        station.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        station.setRoutes(List.of(routeFound));

        List<CoordinateDto> coordinateDtos = createStationDto.getCoordinates();
        List<Coordinate> coordinatesCreated = coordinateService.createAll(coordinateDtos, station);
        station.setCoordinates(coordinatesCreated);

        Station newStation = stationRepository.save(station);

        routeFound.getStations().add(newStation);
        routeRepository.save(routeFound);

        return stationMapper.stationToGetStationDto(newStation);
    }

    @Override
    public Page<GetStationDto> findAllByRouteId(Long routeId, Pageable pageable) {
        Route route = routeRepository
                .findById(routeId)
                .orElseThrow(() -> new ObjectNotFoundException("Route not found."));

        Page<Station> stationPage = stationRepository.findAllByRouteId(route.getId(), pageable);

        return stationPage.map(stationMapper::stationToGetStationDto);
    }

    @Override
    public void delete(Long stationId) {
        Station station = stationRepository
                .findById(stationId)
                .orElseThrow(() -> new ObjectNotFoundException("Station not found."));

        stationRepository.delete(station);
    }
}
