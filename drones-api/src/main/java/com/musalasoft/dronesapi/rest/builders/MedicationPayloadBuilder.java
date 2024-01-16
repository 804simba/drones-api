package com.musalasoft.dronesapi.rest.builders;

import com.musalasoft.dronesapi.model.entity.Medication;
import com.musalasoft.dronesapi.model.payload.medication.MedicationDto;
import com.musalasoft.dronesapi.model.payload.drone.LoadDroneRequest;

public class MedicationPayloadBuilder {
    public static Medication buildMedicationEntity(LoadDroneRequest loadDroneRequest) {
        return Medication.builder().code(loadDroneRequest.getMedicationCode())
                .weight(loadDroneRequest.getMedicationWeight() != null ? loadDroneRequest.getMedicationWeight() : 0.0)
                .name(loadDroneRequest.getMedicationName()).build();
    }

    public static MedicationDto convertToMedicationDto(Medication medication) {
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setId(medication.getId());
        medicationDto.setWeight((medication.getWeight() != null) ? medication.getWeight() : 0.0);
        medicationDto.setName(medication.getName());
        medicationDto.setImage(medication.getImageUrl());
        medicationDto.setCode(medication.getCode());
        return medicationDto;
    }
}
