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
public class UpdateStationDto {
    private String name;

    private List<CoordinateDto> coordinates;
}
