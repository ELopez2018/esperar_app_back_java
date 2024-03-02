package com.example.esperar_app.persistence.dto.route;

import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CreateRouteDto {
    @NotEmpty
    private String name;

    @NotEmpty
    private String from;

    @NotEmpty
    private String to;

    private List<CoordinateDto> coordinates;
}
