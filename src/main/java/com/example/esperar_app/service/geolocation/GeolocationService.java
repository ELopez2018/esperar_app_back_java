package com.example.esperar_app.service.geolocation;

import com.example.esperar_app.exception.ObjectNotFoundException;
import com.example.esperar_app.persistence.dto.geolocation.AddGeolocationDto;
import com.example.esperar_app.persistence.dto.geolocation.GeolocationResponse;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeolocationService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public GeolocationService(
            VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public GeolocationResponse addGeolocationByVehicleId(AddGeolocationDto addGeolocationDto) {
        Vehicle vehicleFound = vehicleRepository
                .findById(addGeolocationDto.getVehicleId())
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found"));

        GeolocationResponse geolocationResponse = new GeolocationResponse();
        geolocationResponse.setLatitude(addGeolocationDto.getLatitude());
        geolocationResponse.setLongitude(addGeolocationDto.getLongitude());
        geolocationResponse.setVehicleId(vehicleFound.getId());

        return geolocationResponse;
    }
}
