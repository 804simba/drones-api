package com.musalasoft.dronesapi.model.payload.dto;

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
