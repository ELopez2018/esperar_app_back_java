package com.example.esperar_app.persistence.dto.station;

import com.example.esperar_app.persistence.dto.coordinate.CoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStationDto {
    private String name;

    private Long routeId;

    private List<CoordinateDto> coordinates;

    private Date createdAt;

    private Date updatedAt;

    private Date deletedAt;
}
