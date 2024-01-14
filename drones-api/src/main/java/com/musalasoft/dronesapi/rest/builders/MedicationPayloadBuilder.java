package com.musalasoft.dronesapi.rest.builders;

import com.musalasoft.dronesapi.model.entity.Medication;
import com.musalasoft.dronesapi.model.payload.dto.MedicationDto;
import com.musalasoft.dronesapi.model.payload.request.LoadDroneRequest;

public class MedicationPayloadBuilder {
    public static Medication buildMedicationEntity(LoadDroneRequest loadDroneRequest) {
        return Medication.builder().code(loadDroneRequest.getMedicationCode()).image(loadDroneRequest.getMedicationImage())
                .weight(loadDroneRequest.getMedicationWeight()).name(loadDroneRequest.getMedicationName()).build();
    }

    public static MedicationDto convertToMedicationDto(Medication medication) {
        MedicationDto medicationDto = new MedicationDto();
        medicationDto.setId(medication.getId());
        medicationDto.setWeight(medication.getWeight());
        medicationDto.setName(medication.getName());
        medicationDto.setImage(medication.getImage());
        medicationDto.setCode(medication.getCode());

        return medicationDto;
    }
}
