package com.musalasoft.dronesapi.model.payload.drone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterDroneRequest implements Serializable {
    @NotEmpty(message = "serial number must not be null")
    @Size(min = 5, max = 100, message = "Serial number must be at most 100 characters")
    @JsonProperty("serial_number")
    private String serialNumber;

    @NotEmpty(message = "drone model must not be null")
    @JsonProperty(value = "model", required = true)
    private String model;

    @NotNull(message = "weight limit must not be null")
    @PositiveOrZero(message = "weight limit must be a non-negative value")
    @Max(value = 500, message = "weight limit must be at most 500")
    @JsonProperty(value = "weight_limit", required = true)
    private Double weightLimit;

    @NotNull(message = "battery capacity must not be null")
    @JsonProperty(value = "battery_capacity", required = true)
    @Max(value = 100, message = "battery capacity must not be greater than 100%")
    @PositiveOrZero(message = "battery capacity must not be negative")
    private Integer batteryCapacity;
}
