package com.musalasoft.dronesapi.rest.service.impl;

import com.musalasoft.dronesapi.core.exception.DroneNotFoundException;
import com.musalasoft.dronesapi.core.exception.DroneRegistrationException;
import com.musalasoft.dronesapi.core.utils.Constants;
import com.musalasoft.dronesapi.core.utils.payload.ResponsePayloadUtility;
import com.musalasoft.dronesapi.core.utils.payload.DroneValidatorUtility;
import com.musalasoft.dronesapi.model.entity.Drone;
import com.musalasoft.dronesapi.model.entity.Medication;
import com.musalasoft.dronesapi.model.enums.DroneState;
import com.musalasoft.dronesapi.model.payload.dto.DroneDto;
import com.musalasoft.dronesapi.model.payload.dto.MedicationDto;
import com.musalasoft.dronesapi.model.payload.request.LoadDroneRequest;
import com.musalasoft.dronesapi.model.payload.request.RegisterDroneRequest;
import com.musalasoft.dronesapi.model.payload.response.BaseResponse;
import com.musalasoft.dronesapi.model.payload.response.BatteryLevelResponse;
import com.musalasoft.dronesapi.model.payload.response.FetchLoadedMedicationsResponse;
import com.musalasoft.dronesapi.model.repository.DroneRepository;
import com.musalasoft.dronesapi.rest.builders.DronePayloadBuilder;
import com.musalasoft.dronesapi.rest.builders.MedicationPayloadBuilder;
import com.musalasoft.dronesapi.rest.service.BatteryLevelAuditService;
import com.musalasoft.dronesapi.rest.service.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;

    private final BatteryLevelAuditService batteryLevelAuditService;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, BatteryLevelAuditService batteryLevelAuditService) {
        this.droneRepository = droneRepository;
        this.batteryLevelAuditService = batteryLevelAuditService;
    }

    @Override
    @Transactional
    public BaseResponse<?> registerDrone(RegisterDroneRequest registerDroneRequest) {
        try {
            boolean droneExists = droneRepository.existsBySerialNumber(registerDroneRequest.getSerialNumber());
            if (droneExists) {
                throw new DroneRegistrationException("registration failed: drone already exists");
            }
        } catch (Exception e) {
            return ResponsePayloadUtility.createFailedResponse("registration failed:" + e.getMessage());
        }
        Drone drone = new Drone();
        drone.setSerialNumber(registerDroneRequest.getSerialNumber());
        drone.setBatteryLevel(registerDroneRequest.getBatteryCapacity());
        drone.setWeightLimit(registerDroneRequest.getWeightLimit());
        drone.setDroneState(DroneState.IDLE);
        drone.setModel(registerDroneRequest.getModel());

        Drone savedDrone = droneRepository.save(drone);
        DroneDto droneDto = DronePayloadBuilder.convertToDroneDto(savedDrone);

        return ResponsePayloadUtility.createSuccessResponse(droneDto, "drone registered successfully");
    }

    @Override
    @Transactional
    public BaseResponse<?> loadDrone(Long droneId, LoadDroneRequest loadDroneRequest) {
        boolean droneExists = droneRepository.existsBySerialNumberAndId(loadDroneRequest.getDroneSerialNumber(), droneId);
        if (!droneExists) {
            String message = String.format("drone with serial number %s not found", loadDroneRequest.getDroneSerialNumber());
            throw new DroneNotFoundException(message);
        }
        Drone drone = droneRepository.findById(droneId).get();
        Medication medication = MedicationPayloadBuilder.buildMedicationEntity(loadDroneRequest);
        DroneValidatorUtility.validateDroneWeightLimit(drone, medication.getWeight());
        DroneValidatorUtility.validateDroneBatteryLevel(drone);
        drone.getMedications().add(medication);
        Drone savedDrone = droneRepository.save(drone);
        DroneDto droneDto = DronePayloadBuilder.convertToDroneDto(savedDrone);
        return ResponsePayloadUtility.createSuccessResponse(droneDto, "drone loaded successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<?> getLoadedMedication(Long droneId) {
        Drone foundDrone = droneRepository.findById(droneId)
                .orElseThrow(() -> new DroneNotFoundException("drone not found"));
        FetchLoadedMedicationsResponse fetchLoadedMedicationsResponse = new FetchLoadedMedicationsResponse();
        fetchLoadedMedicationsResponse.setDroneId(foundDrone.getId());
        fetchLoadedMedicationsResponse.setDroneSerialNumber(foundDrone.getSerialNumber());
        List<MedicationDto> medicationsList = new ArrayList<>();

        foundDrone.getMedications().forEach(medication -> {
            medicationsList.add(MedicationPayloadBuilder.convertToMedicationDto(medication));
        });
        fetchLoadedMedicationsResponse.setMedications(medicationsList);
        return ResponsePayloadUtility.createSuccessResponse(fetchLoadedMedicationsResponse, "fetched medications records successfully for drone with id " + droneId);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<?> getAvailableDronesForLoading(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Drone> availableDrones = droneRepository.findAll(pageable);
        if (availableDrones.isEmpty()) {
            throw new DroneNotFoundException("no available drones were found");
        }
        Page<DroneDto> pagedAvailableDrones = availableDrones.map(DronePayloadBuilder::convertToDroneDto);
        return ResponsePayloadUtility.createSuccessResponse(pagedAvailableDrones,"fetched available drones successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<?> getDroneBatteryLevel(Long droneId) {
        BatteryLevelResponse batteryLevelResponse = new BatteryLevelResponse();
        try {
            Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new DroneNotFoundException("drone not found"));
            batteryLevelResponse.setDroneSerialNumber(drone.getSerialNumber());
            batteryLevelResponse.setBatteryLevel(drone.getBatteryLevel());
        } catch (Exception e) {
            return ResponsePayloadUtility.createFailedResponse("drone not found");
        }
        return ResponsePayloadUtility.createSuccessResponse(batteryLevelResponse, Constants.ResponseMessages.SUCCESS);
    }

    @Override
    public void checkBatteryLevelsSchedule() {
        List<Drone> availableDrones = droneRepository.findAll();
        if (!ObjectUtils.isEmpty(availableDrones)) {
            for (Drone drone : availableDrones) {
                batteryLevelAuditService.auditBatteryLevelCheck(drone.getId(), drone.getBatteryLevel());
            }
        }
    }
}
