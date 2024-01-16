package com.musalasoft.dronesapi.model.payload.medication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MedicationDto {
    @JsonProperty("medication_id")
    private Long id;

    @JsonProperty("medication_name")
    private String name;

    @JsonProperty("medication_weight")
    private Double weight;

    @JsonProperty("medication_code")
    private String code;

    @JsonProperty("medication_image")
    private String image;
}
