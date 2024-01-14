package com.musalasoft.dronesapi.core.exception;

public class DroneBatteryDischargedException extends RuntimeException{
    public DroneBatteryDischargedException() {
    }

    public DroneBatteryDischargedException(String message) {
        super(message);
    }

    public DroneBatteryDischargedException(String message, Throwable cause) {
        super(message, cause);
    }
}
