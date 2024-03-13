package com.example.esperar_app.service.station;

import com.example.esperar_app.mapper.StationMapper;
import com.example.esperar_app.persistence.dto.station.CreateStationDto;
import com.example.esperar_app.persistence.entity.route.Route;
import com.example.esperar_app.persistence.entity.station.Station;
import com.example.esperar_app.persistence.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;

    private final StationMapper stationMapper;

    @Autowired
    public StationServiceImpl(
            StationRepository stationRepository,
            StationMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
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
}
