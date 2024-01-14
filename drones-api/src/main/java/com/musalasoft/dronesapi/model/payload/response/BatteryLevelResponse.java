package com.musalasoft.dronesapi.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatteryLevelResponse implements Serializable {
    @JsonProperty("drone_serial_number")
    private String droneSerialNumber;

    @JsonProperty("battery_level")
    private double batteryLevel;
}
