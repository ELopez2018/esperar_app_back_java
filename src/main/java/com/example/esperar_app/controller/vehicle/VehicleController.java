package com.example.esperar_app.controller.vehicle;

import com.example.esperar_app.mapper.VehicleMapper;
import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.GetVehicle;
import com.example.esperar_app.persistence.dto.inputs.vehicle.UpdateVehicleDto;
import com.example.esperar_app.persistence.entity.Vehicle;
import com.example.esperar_app.service.vehicle.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    @Autowired
    public VehicleController(
            VehicleService vehicleService,
            VehicleMapper vehicleMapper) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<GetVehicle> create(@RequestBody @Valid CreateVehicleDto createVehicleDto) {
        Vehicle newVehicle = vehicleService.create(createVehicleDto);
        GetVehicle getVehicle = vehicleMapper.toGetVehicle(newVehicle);
        return ResponseEntity.ok(getVehicle);
    }

    @GetMapping
    public ResponseEntity<Page<GetVehicle>> findAll(Pageable pageable) {
        Page<GetVehicle> vehiclesPage = vehicleService.findAll(pageable);

        if(vehiclesPage.hasContent()) return ResponseEntity.ok(vehiclesPage);
        else return ResponseEntity.notFound().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<GetVehicle> findById(@PathVariable Long id) {
        GetVehicle vehicle = vehicleService.findById(id);

        if(vehicle != null) return ResponseEntity.ok(vehicle);
        else return ResponseEntity.notFound().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<GetVehicle> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateVehicleDto updateVehicleDto) {
        GetVehicle vehicle = vehicleService.update(id, updateVehicleDto);

        if(vehicle != null) return ResponseEntity.ok(vehicle);
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
