package com.musalasoft.dronesapi.core.exception;

public class DroneAlreadyExistsException extends RuntimeException{
    public DroneAlreadyExistsException() {
    }

    public DroneAlreadyExistsException(final String message) {
        super(message);
    }

    public DroneAlreadyExistsException(final String message, Throwable cause) {
        super(message, cause);
    }
}
