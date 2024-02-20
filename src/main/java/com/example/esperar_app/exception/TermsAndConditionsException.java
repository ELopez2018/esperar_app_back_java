package com.example.esperar_app.exception;

/**
 * Exception thrown when the user has not accepted the terms and conditions
 */
public class TermsAndConditionsException extends RuntimeException {
    public TermsAndConditionsException() {}

    public TermsAndConditionsException(String message) {
        super(message);
    }

    public TermsAndConditionsException(String message, Throwable cause) {
        super(message, cause);
    }
}
