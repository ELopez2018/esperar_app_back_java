package com.example.esperar_app.controller.station;

import com.example.esperar_app.persistence.dto.station.CreateStationDto;
import com.example.esperar_app.persistence.dto.station.GetStationDto;
import com.example.esperar_app.service.station.StationService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<GetStationDto> create(@RequestBody @Valid CreateStationDto createStationDto) {
        logger.info("Create a new station request received.");
        GetStationDto station = stationService.create(createStationDto);
        if(station != null) return ResponseEntity.ok(station);
        else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/by-route/{routeId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Page<GetStationDto>> findAllByRouteId(
            @PathVariable Long routeId,
            Pageable pageable
    ) {
        logger.info("Find all stations by route request received.");
        Page<GetStationDto> stationsPage = stationService.findAllByRouteId(routeId, pageable);
        return ResponseEntity.ok(stationsPage != null ? stationsPage : Page.empty());
    }

    @DeleteMapping("/{stationId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Void> delete(@PathVariable Long stationId) {
        logger.info("Delete station request received.");
        stationService.delete(stationId);
        return ResponseEntity.ok().build();
    }
}
