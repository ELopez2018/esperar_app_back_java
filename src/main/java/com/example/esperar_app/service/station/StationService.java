package com.example.esperar_app.service.station;

import com.example.esperar_app.persistence.dto.station.CreateStationDto;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.station.Station;

import java.util.List;

public interface StationService {
    List<Station> createAll(List<CreateStationDto> stations, Route route);
}
