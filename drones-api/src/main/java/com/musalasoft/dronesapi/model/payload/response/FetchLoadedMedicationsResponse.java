package com.musalasoft.dronesapi.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.musalasoft.dronesapi.model.payload.dto.MedicationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchLoadedMedicationsResponse implements Serializable {
    @JsonProperty("drone_id")
    private Long droneId;

    @JsonProperty("drone_serial_number")
    private String droneSerialNumber;

    @JsonProperty("medications")
    private List<MedicationDto> medications;
}
