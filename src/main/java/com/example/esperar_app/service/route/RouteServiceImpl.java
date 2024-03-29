package com.example.esperar_app.service.route;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.CoordinateMapper;
import com.example.esperar_app.mapper.RouteMapper;
import com.example.esperar_app.mapper.StationMapper;
import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import com.example.esperar_app.persistence.dto.route.AssignVehicleToRouteDto;
import com.example.esperar_app.persistence.dto.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import com.example.esperar_app.persistence.dto.route.UpdateRouteDto;
import com.example.esperar_app.persistence.dto.station.GetStationDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.entity.coordinate.Coordinate;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.station.Station;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.repository.CoordinateRepository;
import com.example.esperar_app.persistence.repository.RouteRepository;
import com.example.esperar_app.persistence.repository.StationRepository;
import com.example.esperar_app.persistence.repository.VehicleRepository;
import com.example.esperar_app.service.coordinate.CoordinateService;
import com.example.esperar_app.service.station.StationService;
import com.example.esperar_app.service.user.UserService;
import com.example.esperar_app.websocket.persistence.dto.InitRouteDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    private final RouteMapper routeMapper;

    private final CoordinateMapper coordinateMapper;

    private final CoordinateService coordinateService;

    private final VehicleRepository vehicleRepository;

    private final CoordinateRepository coordinateRepository;

    private final UserService userService;

    private final StationService stationService;

    private final StationRepository stationRepository;

    private final StationMapper stationMapper;

    private static final Logger logger = LogManager.getLogger();


    @Autowired
    public RouteServiceImpl(
            RouteRepository routeRepository,
            RouteMapper routeMapper,
            CoordinateMapper coordinateMapper,
            CoordinateService coordinateService,
            VehicleRepository vehicleRepository,
            CoordinateRepository coordinateRepository,
            UserService userService,
            StationService stationService,
            StationRepository stationRepository,
            StationMapper stationMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
        this.coordinateMapper = coordinateMapper;
        this.coordinateService = coordinateService;
        this.vehicleRepository = vehicleRepository;
        this.coordinateRepository = coordinateRepository;
        this.userService = userService;
        this.stationService = stationService;
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    /**
     * Create a new route
     * @param createRouteDto - create new route dto
     * @return route created
     */
    @Override
    @Transactional
    public GetRouteDto create(CreateRouteDto createRouteDto) {
        Route route = routeMapper.toEntity(createRouteDto);
        route.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        try {
            Route routeSaved = routeRepository.save(route);
            logger.info("Route created with id: " + routeSaved.getId());

            List<CoordinateDto> coordinateDtos = createRouteDto.getCoordinates();

            List<Coordinate> coordinatesCreated = coordinateService.createAll(coordinateDtos, routeSaved);

            routeSaved.setCoordinates(coordinatesCreated);

            List<Station> stationsCreated = stationService.createAll(createRouteDto.getStations(), routeSaved);

            routeSaved.setStations(stationsCreated);

            return routeMapper.routeToGetRouteDto(routeSaved);
        } catch (Exception e) {
            logger.error("Error creating route");
            throw e;
        }
    }

    /**
     * find all routes paginated
     * @param pageable - pageable data
     * @return - routes paginated
     */
    @Override
    public Page<GetRouteDto> findAll(Pageable pageable) {
        Page<Route> routes = routeRepository.findAll(pageable);

        logger.info("Routes found: " + routes.getTotalElements());

        return routes.map(route -> {
            GetRouteDto routeDto = new GetRouteDto();
            routeDto.setId(route.getId());
            routeDto.setName(route.getName());
            routeDto.setFrom(route.getFrom());
            routeDto.setTo(route.getTo());
            routeDto.setCreatedAt(route.getCreatedAt());
            routeDto.setUpdatedAt(route.getUpdatedAt());
            routeDto.setDeletedAt(route.getDeletedAt());

            List<GetCoordinateDto> coordinateDtos = route.getCoordinates().stream()
                    .map(coordinateMapper::coordinateToGetCoordinateDto)
                    .collect(Collectors.toList());

            routeDto.setCoordinates(coordinateDtos);

            List<GetStationDto> stationDtos = route.getStations().stream()
                    .map(stationMapper::stationToGetStationDto)
                    .collect(Collectors.toList());

            routeDto.setStations(stationDtos);

            return routeDto;
        });
    }


    /**
     * find route by identifier
     * @param id - route identifier
     * @return - route found
     */
    @Override
    public GetRouteDto findById(Long id) {
        Route routeFound = routeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

        GetRouteDto routeDto = routeMapper.routeToGetRouteDto(routeFound);

        routeDto.setCoordinates(new ArrayList<>());

        mapCoordinatesToRouteDto(routeFound, routeDto);

        return routeDto;
    }

    /**
     * update route by identifier
     * @param id - route identifier
     * @param updateRouteDto - update route dto
     * @return - route updated
     */
    @Override
    @Transactional
    public GetRouteDto update(Long id, UpdateRouteDto updateRouteDto) {
        Route route = routeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

        BeanUtils.copyProperties(updateRouteDto, route, getStrings(updateRouteDto));
        route.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        List<Coordinate> coordinatesCreated = coordinateService.createAll(updateRouteDto.getCoordinates(), route);
        route.setCoordinates(coordinatesCreated);

        List<Station> stationsCreated = stationService.createAll(updateRouteDto.getStations(), route);
        route.setStations(stationsCreated);

        logger.info("Route updated with id: " + route.getId());

        Route routeUpdated = routeRepository.save(route);

        logger.info("Route updated with id: " + route.getId());

        return routeMapper.routeToGetRouteDto(routeUpdated);
    }

    /**
     * delete route by identifier
     * @param id - route identifier
     */
    @Override
    public void delete(Long id) {
        Route route = routeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

        routeRepository.delete(route);
    }

    @Override
    public void assignVehicleToRoute(AssignVehicleToRouteDto assignVehicleToRouteDto) {
        try {
            Route routeFound = routeRepository
                    .findById(assignVehicleToRouteDto.getRouteId())
                    .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

            for (Long vehicleId : assignVehicleToRouteDto.getVehiclesIds()) {
                Vehicle vehicleFound = vehicleRepository
                        .findById(vehicleId)
                        .orElseThrow(() -> new ObjectNotFoundException("Vehicle with ID: " + vehicleId + " not found"));

                vehicleFound.setRoute(routeFound);

                List<Vehicle> assignedVehicles = routeFound.getVehicles();
                assignedVehicles.add(vehicleFound);
            }

            routeRepository.save(routeFound);

            logger.info("Vehicles assigned to route: [" + routeFound.getName() + "]");
        } catch (Exception e) {
            logger.error("Error assigning vehicles to route");
            throw e;
        }
    }

    @Override
    public void initRoute(InitRouteDto initRouteDto) {
        Route route = routeRepository
                .findById(initRouteDto.getRouteId())
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

        GetUserDto userFound = userService.findById(initRouteDto.getUserId());

        if(!Objects.equals(userFound.getId(), initRouteDto.getUserId())) {
            logger.error("User not matching with the one logged in");
            throw new RuntimeException("Access denied");
        }

        if(!Objects.equals(userFound.getCurrentVehicle().getRoute().getId(), route.getId())) {
            logger.error("Vehicle not matching with the route selected");
            throw new RuntimeException("Access denied");
        }
    }

    /**
     * map coordinates to route dto
     * @param route - route
     * @param routeDto - route dto
     */
    private void mapCoordinatesToRouteDto(Route route, GetRouteDto routeDto) {
        List<GetCoordinateDto> coordinateDtos = route.getCoordinates().stream()
                .map(coordinateMapper::coordinateToGetCoordinateDto)
                .toList();

        if (routeDto.getCoordinates() == null) routeDto.setCoordinates(new ArrayList<>());

        routeDto.getCoordinates().addAll(coordinateDtos);
    }

}
