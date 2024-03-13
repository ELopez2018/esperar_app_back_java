package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.route.CreateRouteDto;
import com.example.esperar_app.persistence.dto.route.GetRouteDto;
import com.example.esperar_app.persistence.entity.route.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        CoordinateMapper.class,
        StationMapper.class
})
public interface RouteMapper {

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "from", target = "from"),
            @Mapping(source = "to", target = "to"),
            @Mapping(target = "coordinates", source = "coordinates"),
            @Mapping(target = "stations", source = "stations"),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "vehicles", ignore = true)
    })
    Route toEntity(CreateRouteDto createRouteDto);

    @Mappings({
            @Mapping(source = "coordinates", target = "coordinates"),
    })
    GetRouteDto routeToGetRouteDto(Route routeSaved);
}
