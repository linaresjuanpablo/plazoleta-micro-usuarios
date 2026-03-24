package com.gastropolis.usuarios.infrastructure.exceptionhandler;

public enum ExceptionResponse {

    USER_ALREADY_EXISTS("User already exists with the provided email or DNI"),
    USER_NOT_FOUND("User not found"),
    INVALID_AGE("User does not meet the age requirement"),
    INVALID_CREDENTIALS("Invalid email or password"),
    VALIDATION_ERROR("Validation failed for one or more fields"),
    INTERNAL_ERROR("An internal server error occurred");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
