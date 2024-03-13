package com.example.esperar_app.service.station;

import com.example.esperar_app.persistence.dto.station.CreateStationDto;
import com.example.esperar_app.persistence.dto.station.GetStationDto;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.station.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StationService {
    List<Station> createAll(List<CreateStationDto> stations, Route route);

    GetStationDto create(CreateStationDto createStationDto);

    Page<GetStationDto> findAllByRouteId(Long routeId, Pageable pageable);

    void delete(Long stationId);
}
