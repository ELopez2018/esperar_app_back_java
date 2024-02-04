package com.example.esperar_app.service.route;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.mapper.RouteMapper;
import com.example.esperar_app.persistence.dto.inputs.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.inputs.route.UpdateRouteDto;
import com.example.esperar_app.persistence.entity.route.Route;
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
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Autowired
    public RouteServiceImpl(
            RouteRepository routeRepository,
            RouteMapper routeMapper) {
        this.routeRepository = routeRepository;
        this.routeMapper = routeMapper;
    }

    @Override
    public Route create(CreateRouteDto createRouteDto) {
        Route route = routeMapper.toEntity(createRouteDto);
        route.setCoordinates(null);
        route.setCreatedAt(Date.valueOf(LocalDate.now()));
        return routeRepository.save(route);
    }

    @Override
    public Page<Route> findAll(Pageable pageable) {
        return routeRepository.findAll(pageable);
    }

    @Override
    public Route findById(Long id) {
        return routeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Route not found"));
    }

    @Override
    public Route update(Long id, UpdateRouteDto updateRouteDto) {
        Route route = findById(id);
        BeanUtils.copyProperties(updateRouteDto, route, getStrings(updateRouteDto));

        route.setUpdatedAt(Date.valueOf(LocalDate.now()));
        return routeRepository.save(route);
    }

    @Override
    public void delete(Long id) {
        Route route = findById(id);
        routeRepository.delete(route);
    }
}
