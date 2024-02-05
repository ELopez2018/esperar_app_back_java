package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.coordinate.CreateCoordinateDto;
import com.example.esperar_app.persistence.dto.coordinate.GetCoordinateDto;
import com.example.esperar_app.persistence.entity.coordinate.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CoordinateMapper {

    @Mappings({
            @Mapping(source = "latitude", target = "latitude"),
            @Mapping(source = "longitude", target = "longitude"),
            @Mapping(source = "routeId", target = "route.id"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Coordinate toEntity(CreateCoordinateDto createCoordinateDto);

    GetCoordinateDto coordinateToGetCoordinateDto(Coordinate coordinateSaved);
}
