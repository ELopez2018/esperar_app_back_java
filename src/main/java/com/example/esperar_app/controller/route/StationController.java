package com.example.esperar_app.controller.route;

import com.example.esperar_app.service.station.StationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routes/stations")
public class StationController {

    private final StationService stationService;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

//    @PostMapping
//    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
//    public ResponseEntity<GetStationDto> create(@RequestBody @Valid CreateStationDto createStationDto) {
//        logger.info("Create a new station request received.");
//        GetStationDto station = stationService.create(createStationDto);
//        if(station != null) return ResponseEntity.ok(station);
//        else return ResponseEntity.badRequest().build();
//    }
}
