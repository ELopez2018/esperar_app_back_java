package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.inputs.route.CreateRouteDto;
import com.example.esperar_app.persistence.entity.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RouteMapper {

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "from", target = "from"),
            @Mapping(source = "to", target = "to"),
            @Mapping(target = "coordinates", ignore = true)
    })
    Route toEntity(CreateRouteDto createRouteDto);

}