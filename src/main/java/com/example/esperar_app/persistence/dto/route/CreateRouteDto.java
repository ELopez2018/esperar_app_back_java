package com.example.esperar_app.persistence.dto.inputs.route;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateRouteDto {
    @NotEmpty
    private String name;

    @NotEmpty
    private String from;

    @NotEmpty
    private String to;
}
