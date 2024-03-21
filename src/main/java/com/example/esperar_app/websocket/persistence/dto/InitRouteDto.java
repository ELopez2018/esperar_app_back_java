package com.example.esperar_app.websocket.persistence.dto;

import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class InitRouteDto implements Serializable {
    private Long userId;

    private Long routeId;

    private List<CoordinateDto> coordinates;
}
