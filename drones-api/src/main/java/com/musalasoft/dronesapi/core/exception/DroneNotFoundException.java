package com.musalasoft.dronesapi.core.exception;

public class DroneNotFoundException extends RuntimeException{
    public DroneNotFoundException() {
    }

    public DroneNotFoundException(String message) {
        super(message);
    }

    public DroneNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
