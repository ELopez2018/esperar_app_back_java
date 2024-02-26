package com.example.esperar_app.controller.vehicle;

import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.dto.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.repository.VehicleRepository;
import com.example.esperar_app.service.vehicle.VehicleService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    private final VehicleRepository vehicleRepository;

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public VehicleController(
            VehicleService vehicleService,
            VehicleRepository vehicleRepository) {
        this.vehicleService = vehicleService;
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<GetVehicleDto> create(@RequestBody @Valid CreateVehicleDto createVehicleDto) {
        logger.info("Create a new vehicle request received.");
        GetVehicleDto newVehicle = vehicleService.create(createVehicleDto);
        return newVehicle != null ? ResponseEntity.ok(newVehicle) : ResponseEntity.badRequest().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Page<GetVehicleDto>> findAll(Pageable pageable) {
        logger.info("Find all vehicles request received.");
        Page<GetVehicleDto> vehiclesPage = vehicleService.findAll(pageable);
        return ResponseEntity.ok(vehiclesPage != null ? vehiclesPage : Page.empty());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<GetVehicleDto> findById(@PathVariable Long id) {
        logger.info("Find vehicle with id: [" + id + "] request received.");
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<GetVehicleDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateVehicleDto updateVehicleDto) {
        logger.info("Update vehicle with id: [" + id + "] request received.");
        return ResponseEntity.ok(vehicleService.update(id, updateVehicleDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Delete vehicle with id: [" + id + "] request received.");
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignDriver/{vehicleId}/{driverId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Void> assignDriver(@PathVariable Long vehicleId, @PathVariable Long driverId) {
        logger.info("Assign driver with id: [" + driverId + "] to vehicle with id: [" + vehicleId + "]" +
                "request received.");
        Vehicle vehicle = vehicleService.assignDriver(vehicleId, driverId);
        vehicleRepository.save(vehicle);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle-drivers/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<List<GetUserDto>> findVehicleDrivers(@PathVariable Long id) {
        logger.info("Find drivers for vehicle with id: [" + id + "] request received.");
        List<GetUserDto> drivers = vehicleService.findVehicleDrivers(id);
        return ResponseEntity.ok(drivers != null ? drivers : List.of());
    }

    @GetMapping("/soat-expiration-soon")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Page<GetVehicleDto>> findVehiclesWithSoatSoonToExpire(Pageable pageable) {
        logger.info("Find vehicles with SOAT soon to expire request received.");
        Page<GetVehicleDto> vehiclesPage = vehicleService.findVehiclesWithSoatSoonToExpire(pageable);
        return ResponseEntity.ok(vehiclesPage != null ? vehiclesPage : Page.empty());
    }

    @GetMapping("/tecnomechanical-expiration-soon")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Page<GetVehicleDto>> findVehiclesWithTecnomechanicalSoonToExpire(Pageable pageable) {
        logger.info("Find vehicles with tecnomechanical soon to expire request received.");
        Page<GetVehicleDto> vehiclesPage = vehicleService.findVehiclesWithTecnomechanicalSoonToExpire(pageable);
        return ResponseEntity.ok(vehiclesPage != null ? vehiclesPage : Page.empty());
    }
}
