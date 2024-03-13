package com.example.esperar_app.mapper;

import com.example.esperar_app.persistence.dto.station.CreateStationDto;
import com.example.esperar_app.persistence.dto.station.GetStationDto;
import com.example.esperar_app.persistence.entity.station.Station;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CoordinateMapper.class})
public interface StationMapper {

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deletedAt", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "routes", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "coordinates", source = "coordinates"),
    })
    Station createStationDtoToStation(CreateStationDto stationDto);

    GetStationDto stationToGetStationDto(Station station);
}
