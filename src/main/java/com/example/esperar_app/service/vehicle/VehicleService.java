package com.example.esperar_app.service.vehicle;

import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VehicleService {
    GetVehicleDto create(CreateVehicleDto createVehicleDto);

    Page<GetVehicleDto> findAll(Pageable pageable);

    GetVehicleDto findById(Long id);

    GetVehicleDto update(Long id, UpdateVehicleDto updateVehicleDto);

    void delete(Long id);

    Vehicle assignDriver(Long id, Long driverId);

    List<GetUserDto> findVehicleDrivers(Long id);
}
