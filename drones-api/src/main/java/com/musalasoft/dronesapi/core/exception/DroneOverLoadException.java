package com.musalasoft.dronesapi.core.exception;

public class DroneOverLoadException extends RuntimeException{
    public DroneOverLoadException() {
    }

    public DroneOverLoadException(String message) {
        super(message);
    }

    public DroneOverLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
