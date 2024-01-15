package com.musalasoft.dronesapi.core.exception.advice;

import com.musalasoft.dronesapi.core.exception.DroneBatteryDischargedException;
import com.musalasoft.dronesapi.core.exception.DroneNotFoundException;
import com.musalasoft.dronesapi.core.exception.DroneOverLoadException;
import com.musalasoft.dronesapi.core.exception.DroneAlreadyExistsException;
import com.musalasoft.dronesapi.core.utils.Constants;
import com.musalasoft.dronesapi.model.payload.response.BaseResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler {
    @ExceptionHandler(DroneAlreadyExistsException.class)
    public BaseResponse<?> droneAlreadyExistsException(DroneAlreadyExistsException exception) {
        return BaseResponse.builder().responseCode(BAD_REQUEST.value())
                .responseMessage(exception.getMessage()).data(null).build();
    }

    @ExceptionHandler(DroneNotFoundException.class)
    public BaseResponse<?> droneNotFoundException(DroneNotFoundException exception) {
        return BaseResponse.builder().responseCode(HttpStatus.NOT_FOUND.value())
                .responseMessage(exception.getMessage()).data(null).build();
    }

    @ExceptionHandler(DroneOverLoadException.class)
    public BaseResponse<?> droneOverloadException(DroneOverLoadException exception) {
        return BaseResponse.builder().responseCode(BAD_REQUEST.value())
                .responseMessage(exception.getMessage()).data(null).build();
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public BaseResponse<?> internalServerError(HttpServerErrorException exception) {
        return BaseResponse.builder().responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .responseMessage(exception.getMessage()).data(null)
                .build();
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exception(Exception exception) {
        return BaseResponse.builder().responseCode(BAD_REQUEST.value())
                .responseMessage(exception.getMessage()).data(null).build();
    }

    @ExceptionHandler(DroneBatteryDischargedException.class)
    public BaseResponse<?> droneBatteryDischargedException(DroneBatteryDischargedException exception) {
        return BaseResponse.builder().responseCode(BAD_REQUEST.value()).responseMessage(exception.getMessage())
                .data(null).build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        String errorMessage = fieldErrors.get(0).getDefaultMessage();
        return BaseResponse.builder().responseMessage(errorMessage).responseCode(Constants.ResponseStatusCode.FAILED).build();
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public BaseResponse<?> methodArgumentNotValidException(IllegalArgumentException exception) {
        return BaseResponse.builder().responseMessage(exception.getMessage())
                .responseCode(Constants.ResponseStatusCode.FAILED).build();
    }
}
