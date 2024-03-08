package com.example.esperar_app.persistence.dto.route;

import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UpdateRouteDto {
    private String name;

    private String from;

    private String to;

    private List<CoordinateDto> coordinates;
}
