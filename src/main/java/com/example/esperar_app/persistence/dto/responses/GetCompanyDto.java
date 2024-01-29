package com.example.esperar_app.persistence.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @AllArgsConstructor
public class GetCompanyDto {
    private Long id;
    private List<GetVehicleDto> vehicles;

    public GetCompanyDto() {
        this.vehicles = new ArrayList<>();
    }
}
