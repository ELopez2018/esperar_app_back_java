package com.example.esperar_app.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AlreadyExistError extends RuntimeException {
    private final String resourceName;

    public AlreadyExistError(String resourceName) {
        this.resourceName = resourceName;
    }
}
