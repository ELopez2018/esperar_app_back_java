package com.example.esperar_app.service.vehicle;

import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.dto.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.utils.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VehicleService {
    GetVehicleDto create(CreateVehicleDto createVehicleDto);

    Page<GetVehicleDto> findAll(Pageable pageable);

    GetVehicleDto findById(Long id);

    GetVehicleDto update(Long id, UpdateVehicleDto updateVehicleDto);

    void delete(Long id);

    Vehicle assignDriver(Long id, Long driverId);

    List<GetUserDto> findVehicleDrivers(Long id);

    Page<GetVehicleDto> findVehiclesWithSoatSoonToExpire(Pageable pageable);

    Page<GetVehicleDto> findVehiclesWithTecnomechanicalSoonToExpire(Pageable pageable);

    boolean uploadVehicleDocument(MultipartFile file, Long vehicleId, ImageType imageType);

    Page<GetVehicleDto> findVehiclesByRouteId(Pageable pageable, Long routeId);

    Page<GetVehicleDto> findVehiclesByCompanyId(Pageable pageable, Long companyId);
}
