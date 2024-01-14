package com.musalasoft.dronesapi.core.exception;

public class DroneRegistrationException extends RuntimeException{
    public DroneRegistrationException() {
    }

    public DroneRegistrationException(final String message) {
        super(message);
    }

    public DroneRegistrationException(final String message, Throwable cause) {
        super(message, cause);
    }
}
