package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.inputs.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.inputs.vehicle.GetVehicle;
import com.example.esperar_app.persistence.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        UserMapper.class,
})
public interface VehicleMapper {

    @Mappings({
            @Mapping(source = "owner", target = "owner"),
            @Mapping(source = "driver", target = "driver"),
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "brand", target = "brand"),
            @Mapping(source = "model", target = "model"),
            @Mapping(source = "licensePlate", target = "licensePlate"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "year", target = "year"),
            @Mapping(source = "capacity", target = "capacity"),
            @Mapping(source = "occupancy", target = "occupancy"),
            @Mapping(source = "cylinderCapacity", target = "cylinderCapacity"),
    })
    GetVehicle toGetVehicle(Vehicle vehicle);

    @Mappings({
            @Mapping(target = "brand", source = "brand"),
    })
    Vehicle toEntity(CreateVehicleDto createVehicleDto);

    Vehicle toEntity(GetVehicle getVehicle);

    @Mappings({
            @Mapping(target = "owner.id", source = "ownerId"),
            @Mapping(target = "driver.id", source = "driverId")
    })
    Vehicle createVehicleToEntity(CreateVehicleDto createVehicleDto);

    List<GetVehicle> toGetVehicles(List<Vehicle> vehicles);
}
