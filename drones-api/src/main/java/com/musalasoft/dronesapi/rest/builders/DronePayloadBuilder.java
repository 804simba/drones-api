package com.musalasoft.dronesapi.rest.builders;

import com.musalasoft.dronesapi.model.entity.Drone;
import com.musalasoft.dronesapi.model.payload.dto.DroneDto;
import com.musalasoft.dronesapi.model.payload.dto.MedicationDto;

import java.util.Set;
import java.util.stream.Collectors;

public class DronePayloadBuilder {
    public static DroneDto convertToDroneDto(Drone drone) {
        DroneDto droneDto = new DroneDto();
        droneDto.setId(drone.getId());
        droneDto.setSerialNumber(drone.getSerialNumber());
        droneDto.setBatteryLevel(drone.getBatteryLevel());
        droneDto.setWeightLimit(drone.getWeightLimit());
        droneDto.setModel(drone.getModel());
        droneDto.setState(drone.getDroneState().name());
        Set<MedicationDto> medications = null;
        if (drone.getMedications() != null) {
            medications = drone.getMedications().stream()
                    .map(MedicationPayloadBuilder::convertToMedicationDto)
                    .collect(Collectors.toSet());
        }
        droneDto.setMedications(medications);
        return droneDto;
    }
}
