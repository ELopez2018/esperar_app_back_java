package com.example.esperar_app.controller.vehicle;

import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.dto.user.GetUserDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import com.example.esperar_app.persistence.repository.VehicleRepository;
import com.example.esperar_app.service.vehicle.VehicleService;
import jakarta.validation.Valid;
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
        GetVehicleDto newVehicle = vehicleService.create(createVehicleDto);
        return newVehicle != null ? ResponseEntity.ok(newVehicle) : ResponseEntity.badRequest().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<Page<GetVehicleDto>> findAll(Pageable pageable) {
        Page<GetVehicleDto> vehiclesPage = vehicleService.findAll(pageable);
        return ResponseEntity.ok(vehiclesPage != null ? vehiclesPage : Page.empty());
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO', 'DRIVER')")
    public ResponseEntity<GetVehicleDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.findById(id));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<GetVehicleDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateVehicleDto updateVehicleDto) {
        return ResponseEntity.ok(vehicleService.update(id, updateVehicleDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/assignDriver/{vehicleId}/{driverId}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<Void> assignDriver(@PathVariable Long vehicleId, @PathVariable Long driverId) {
        Vehicle vehicle = vehicleService.assignDriver(vehicleId, driverId);
        vehicleRepository.save(vehicle);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vehicle-drivers/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CEO')")
    public ResponseEntity<List<GetUserDto>> findVehicleDrivers(@PathVariable Long id) {
        List<GetUserDto> drivers = vehicleService.findVehicleDrivers(id);
        return ResponseEntity.ok(drivers != null ? drivers : List.of());
    }
}
