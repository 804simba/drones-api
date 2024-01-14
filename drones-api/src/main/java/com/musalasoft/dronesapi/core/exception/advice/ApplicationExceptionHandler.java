package com.musalasoft.dronesapi.core.exception.advice;

import com.musalasoft.dronesapi.core.exception.DroneBatteryDischargedException;
import com.musalasoft.dronesapi.core.exception.DroneNotFoundException;
import com.musalasoft.dronesapi.core.exception.DroneOverLoadException;
import com.musalasoft.dronesapi.core.exception.DroneRegistrationException;
import com.musalasoft.dronesapi.model.payload.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(DroneRegistrationException.class)
    public BaseResponse<?> droneRegistrationException(DroneRegistrationException exception) {
        return BaseResponse.builder().responseCode(HttpStatus.BAD_REQUEST.value())
                .responseMessage(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public BaseResponse<?> droneNotFoundException(DroneNotFoundException exception) {
        return BaseResponse.builder().responseCode(HttpStatus.NOT_FOUND.value())
                .responseMessage(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(DroneOverLoadException.class)
    public BaseResponse<?> droneOverloadException(DroneOverLoadException exception) {
        return BaseResponse.builder().responseCode(HttpStatus.BAD_REQUEST.value())
                .responseMessage(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public BaseResponse<?> internalServerError(HttpServerErrorException exception) {
        return BaseResponse.builder().responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseMessage(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exception(Exception exception) {
        return BaseResponse.builder().responseCode(HttpStatus.BAD_REQUEST.value())
                .responseMessage(exception.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(DroneBatteryDischargedException.class)
    public BaseResponse<?> droneBatteryDischargedException(DroneBatteryDischargedException exception) {
        return BaseResponse.builder().responseCode(HttpStatus.BAD_REQUEST.value())
                .responseMessage(exception.getMessage())
                .data(null)
                .build();
    }
}
