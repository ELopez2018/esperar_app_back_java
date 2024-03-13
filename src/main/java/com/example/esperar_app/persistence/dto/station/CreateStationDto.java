package com.example.esperar_app.persistence.dto.station;

import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateStationDto {
    private String name;

    private Long routeId;

    private List<CoordinateDto> coordinates;
}
