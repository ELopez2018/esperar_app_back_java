package com.example.esperar_app.dto.responses;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class ShowPermission implements Serializable {

    private long id;

    private String operation;

    private String httpMethod;

    private String module;

    private String role;
}
