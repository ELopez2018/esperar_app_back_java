package com.example.esperar_app.persistence.dto.inputs.route;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateRouteDto {
    private String name;

    private String from;

    private String to;
}