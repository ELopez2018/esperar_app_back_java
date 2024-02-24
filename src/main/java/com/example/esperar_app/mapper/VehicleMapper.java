package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.vehicle.CreateVehicleDto;
import com.example.esperar_app.persistence.dto.vehicle.GetVehicleDto;
import com.example.esperar_app.persistence.entity.vehicle.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        UserMapper.class,
})
public interface VehicleMapper {

    GetVehicleDto toGetVehicleDto(Vehicle vehicle);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "drivers", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Vehicle toEntity(CreateVehicleDto createVehicleDto);

    @Mappings({
            @Mapping(source = "status", target = "status")
    })
    GetVehicleDto toGetVehicle(Vehicle vehicle);
}
