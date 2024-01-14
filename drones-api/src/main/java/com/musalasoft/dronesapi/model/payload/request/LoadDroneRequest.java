package com.musalasoft.dronesapi.model.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoadDroneRequest implements Serializable {

    @NotEmpty(message = "drone serial number must not be null")
    @JsonProperty("drone_serial_number")
    private String droneSerialNumber;

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "medication name must only contain letters, numbers, '-', or '_'")
    @NotEmpty(message = "medication name must not be null")
    @JsonProperty(value = "medication_name", required = true)
    private String medicationName;

    @NotNull(message = "medication weight must not be null")
    @JsonProperty(value = "medication_weight", required = true)
    private Double medicationWeight;

    @Pattern(regexp = "^[A-Z0-9_]+$", message = "medication code must only contain upper case letters, numbers, or '_'")
    @NotEmpty(message = "medication code must not be null")
    @JsonProperty(value = "medication_code", required = true)
    private String medicationCode;

    @NotNull(message = "medication image must not be null")
    @JsonProperty(value = "medication_image", required = true)
    private String medicationImage;
}
