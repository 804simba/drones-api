package com.musalasoft.dronesapi.model.payload.drone;

import com.musalasoft.dronesapi.model.payload.medication.MedicationDto;
import lombok.Data;

import java.util.Set;

@Data
public class DroneDto {
    private Long id;

    private String serialNumber;

    private String model;

    private double weightLimit;

    private double batteryLevel;

    private String state;

    private Set<MedicationDto> medications;
}
