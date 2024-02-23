package com.example.esperar_app.exception;

import com.example.esperar_app.persistence.dto.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle all exceptions
     * @param exception the exception
     * @return a response entity with the error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception exception) {
        ApiError error = new ApiError();
        error.setMessage("ERROR, PLEASE CHECK SERVER LOGS");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(500);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Handle the ObjectNotFoundException
     * @param exception the exception
     * @return a response entity with the error 404
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handlerObjectNotFoundException(ObjectNotFoundException exception) {
        ApiError error = new ApiError();
        error.setMessage("The requested resource was not found");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(404);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handle the AccessDeniedException
     * @param exception the exception
     * @return a response entity with the error 403
     */
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

    /**
     * Handle the MethodArgumentNotValidException
     * @return a map with the errors in the request and response status 400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> map =  new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return map;
    }

    /**
     * Handle the TermsAndConditionsException
     * @param exception the exception
     * @return a response entity with the error 400
     */
    @ExceptionHandler(TermsAndConditionsException.class)
    public ResponseEntity<?> handlerTermsAndConditionsException(TermsAndConditionsException exception) {
        ApiError error = new ApiError();
        error.setMessage("You must accept the terms and conditions to use the application");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<?> handlerPasswordMismatchException(PasswordMismatchException exception) {
        ApiError error = new ApiError();
        error.setMessage("The password does not match the confirm password");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handlerInvalidTokenException(InvalidTokenException exception) {
        ApiError error = new ApiError();
        error.setMessage("The token is invalid");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<?> handlerIncorrectPasswordException(IncorrectPasswordException exception) {
        ApiError error = new ApiError();
        error.setMessage("The password is incorrect");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AlreadyExistError.class)
    public ResponseEntity<?> handleAlreadyExistError(AlreadyExistError exception) {
        String resourceName = exception.getResourceName();

        ApiError error = new ApiError();
        error.setMessage("Duplicate entry or data integrity violation, check info: [" + resourceName + "]");
        error.setBackedMessage(exception.getLocalizedMessage());
        error.setTime(LocalDateTime.now());
        error.setHttpCode(400);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
