package com.example.esperar_app.service.route;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.CoordinateMapper;
import com.example.esperar_app.mapper.RouteMapper;
import com.example.esperar_app.persistence.dto.company.GetCompanyDto;
import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import com.example.esperar_app.persistence.dto.inputs.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.inputs.route.UpdateRouteDto;
import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.security.User;
import com.example.esperar_app.persistence.repository.RouteRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.esperar_app.service.vehicle.VehicleServiceImpl.getStrings;

@Service
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    private final RouteMapper routeMapper;

    private final CoordinateMapper coordinateMapper;

    @Autowired
    public RouteServiceImpl(
            RouteRepository routeRepository,
            RouteMapper routeMapper,
            CoordinateMapper coordinateMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
        this.coordinateMapper = coordinateMapper;
    }

    /**
     * Create a new route
     * @param createRouteDto - create new route dto
     * @return route created
     */
    @Override
    public GetRouteDto create(CreateRouteDto createRouteDto) {
        Route route = routeMapper.toEntity(createRouteDto);
        route.setCoordinates(null);
        route.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        Route routeSaved = routeRepository.save(route);

        return routeMapper.routeToGetRouteDto(routeSaved);
    }

    /**
     * find all routes paginated
     * @param pageable - pageable data
     * @return - routes paginated
     */
    @Override
    public Page<GetRouteDto> findAll(Pageable pageable) {
        Page<Route> routes = routeRepository.findAll(pageable);

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
    public GetRouteDto update(Long id, UpdateRouteDto updateRouteDto) {
        Route route = routeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));

        BeanUtils.copyProperties(updateRouteDto, route, getStrings(updateRouteDto));
        route.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        Route routeUpdated = routeRepository.save(route);
        GetRouteDto routeDto = routeMapper.routeToGetRouteDto(routeUpdated);

        mapCoordinatesToRouteDto(routeUpdated, routeDto);

        return routeDto;
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
