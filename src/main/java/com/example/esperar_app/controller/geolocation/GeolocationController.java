package com.example.esperar_app.controller.geolocation;

import com.example.esperar_app.persistence.dto.geolocation.AddGeolocationDto;
import com.example.esperar_app.persistence.dto.geolocation.GeolocationResponse;
import com.example.esperar_app.service.geolocation.GeolocationService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GeolocationController {
    private static final Logger logger = LogManager.getLogger();

    private final GeolocationService geolocationService;

    public GeolocationController(GeolocationService geolocationService) {
        this.geolocationService = geolocationService;
    }

    @MessageMapping("/addGeolocation")
    @SendTo("/topic/geolocation")
    public GeolocationResponse create(@Payload @Valid AddGeolocationDto addGeolocationDto) {
        logger.info("Create a new geolocation request received.");
        return geolocationService.addGeolocationByVehicleId(addGeolocationDto);
    }

}
