package com.example.esperar_app.exception;

import com.example.esperar_app.persistence.dto.api.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception exception) {
        ApiError error = new ApiError();
        error.setMessage("ERROR, PLEASE CHECK SERVER LOGS");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(500);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handlerAccessDeniedException( AccessDeniedException exception) {
        ApiError error = new ApiError();
        error.setMessage("Access denied, your not have permission to access this resource," +
                "please contact the administrator if you think this is a mistake");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(403);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ApiError error = new ApiError();

        error.setMessage("ERROR: The request body is invalid or missing some fields");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
