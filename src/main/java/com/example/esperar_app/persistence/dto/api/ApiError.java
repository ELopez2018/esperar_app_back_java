package com.example.esperar_app.persistence.dto.api;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ApiError implements Serializable {

    private String backedMessage;

    private String message;

    private int httpCode;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime time;

    public void setBackedMessage(String backedMessage) {
        this.backedMessage = backedMessage;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
