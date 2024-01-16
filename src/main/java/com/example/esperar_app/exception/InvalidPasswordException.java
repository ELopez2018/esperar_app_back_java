package com.example.esperar_app.exception;

/**
 * InvalidPasswordException is a custom exception for invalid password.
 */
public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() { }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
