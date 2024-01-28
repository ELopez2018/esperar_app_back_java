package com.example.esperar_app.service.vehicle;

import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.GetVehicle;
import com.example.esperar_app.persistence.dto.inputs.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.entity.Vehicle;
import com.example.esperar_app.persistence.entity.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {
    Vehicle create(CreateVehicleDto createVehicleDto);

    Page<GetVehicle> findAll(Pageable pageable);

    GetVehicle findById(Long id);

    GetVehicle update(Long id, UpdateVehicleDto updateVehicleDto);

    void delete(Long id);

    Vehicle assignDriver(Long id, Long driverId);

    List<User> findVehicleDrivers(Long id);
}
