package com.musalasoft.dronesapi.rest.service.impl;

import com.musalasoft.dronesapi.core.exception.DroneAlreadyExistsException;
import com.musalasoft.dronesapi.core.exception.DroneNotFoundException;
import com.musalasoft.dronesapi.core.utils.Constants;
import com.musalasoft.dronesapi.core.utils.payload.DroneModelMapper;
import com.musalasoft.dronesapi.core.utils.payload.DroneValidatorUtility;
import com.musalasoft.dronesapi.core.utils.payload.PaginationPayloadUtility;
import com.musalasoft.dronesapi.core.utils.payload.ResponsePayloadUtility;
import com.musalasoft.dronesapi.model.entity.Drone;
import com.musalasoft.dronesapi.model.entity.Media;
import com.musalasoft.dronesapi.model.entity.Medication;
import com.musalasoft.dronesapi.model.enums.DroneModel;
import com.musalasoft.dronesapi.model.enums.DroneState;
import com.musalasoft.dronesapi.model.payload.base.BaseResponse;
import com.musalasoft.dronesapi.model.payload.drone.DroneBatteryLevelResponse;
import com.musalasoft.dronesapi.model.payload.drone.DroneDto;
import com.musalasoft.dronesapi.model.payload.drone.LoadDroneRequest;
import com.musalasoft.dronesapi.model.payload.drone.RegisterDroneRequest;
import com.musalasoft.dronesapi.model.payload.medication.FetchLoadedMedicationsResponse;
import com.musalasoft.dronesapi.model.payload.medication.MedicationDto;
import com.musalasoft.dronesapi.model.payload.pagination.PagedResponse;
import com.musalasoft.dronesapi.model.repository.DroneRepository;
import com.musalasoft.dronesapi.model.repository.MediaRepository;
import com.musalasoft.dronesapi.model.repository.MedicationRepository;
import com.musalasoft.dronesapi.rest.builders.DronePayloadBuilder;
import com.musalasoft.dronesapi.rest.builders.MedicationPayloadBuilder;
import com.musalasoft.dronesapi.rest.service.DroneBatteryLevelAuditService;
import com.musalasoft.dronesapi.rest.service.DroneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DroneServiceImpl implements DroneService {
    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;

    private final MediaRepository mediaRepository;

    private final DroneBatteryLevelAuditService droneBatteryLevelAuditService;

    @Autowired
    public DroneServiceImpl(DroneRepository droneRepository, DroneBatteryLevelAuditService droneBatteryLevelAuditService, MedicationRepository medicationRepository, MediaRepository mediaRepository) {
        this.droneRepository = droneRepository;
        this.droneBatteryLevelAuditService = droneBatteryLevelAuditService;
        this.medicationRepository = medicationRepository;
        this.mediaRepository = mediaRepository;
    }

    @Override
    @Transactional
    public BaseResponse<?> registerDrone(RegisterDroneRequest registerDroneRequest) {
        boolean droneExists = droneRepository.existsBySerialNumber(registerDroneRequest.getSerialNumber());
        if (droneExists) {
            throw new DroneAlreadyExistsException("registration failed: drone already exists");
        }
        Drone drone = new Drone();
        drone.setSerialNumber(registerDroneRequest.getSerialNumber());
        drone.setBatteryLevel(registerDroneRequest.getBatteryCapacity());
        drone.setWeightLimit(registerDroneRequest.getWeightLimit());
        drone.setDroneState(DroneState.IDLE);
        DroneModel droneModel = DroneModelMapper.mapModel(registerDroneRequest.getModel());
        drone.setModel(droneModel);
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
        DroneValidatorUtility.validateDroneWeightLimit(drone, loadDroneRequest.getMedicationWeight());
        DroneValidatorUtility.validateDroneBatteryLevel(drone);
        Medication medication = MedicationPayloadBuilder.buildMedicationEntity(loadDroneRequest);
        Optional<Media> media = mediaRepository.findById(loadDroneRequest.getMedicationImageId());
        String imageUrl  = ((media.isEmpty()) ? Constants.Media.PLACEHOLDER_URL : media.get().getUrl());
        medication.setImageUrl(imageUrl);
        medication.setDrone(drone);
        medicationRepository.save(medication);

        drone.setDroneState(DroneState.LOADED);
        Drone savedDrone = droneRepository.save(drone);
        DroneDto droneDto = DronePayloadBuilder.convertToDroneDto(savedDrone);
        return ResponsePayloadUtility.createSuccessResponse(droneDto, "drone loaded successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<?> getLoadedMedication(Long droneId) {
        Drone foundDrone = droneRepository.findMedicationsByDroneId(droneId)
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
        PagedResponse<DroneDto> pagedResponse = PaginationPayloadUtility.resolvePaginationMetaData(pagedAvailableDrones, pageNumber, pageSize, "success", Constants.ResponseStatusCode.SUCCESS, "fetched available drones");
        return ResponsePayloadUtility.createSuccessResponse(pagedResponse,"fetched available drones successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<?> getDroneBatteryLevel(Long droneId) {
        DroneBatteryLevelResponse droneBatteryLevelResponse = new DroneBatteryLevelResponse();
        try {
            Drone drone = droneRepository.findById(droneId).orElseThrow(() -> new DroneNotFoundException("drone not found"));
            droneBatteryLevelResponse.setDroneSerialNumber(drone.getSerialNumber());
            droneBatteryLevelResponse.setBatteryLevel(drone.getBatteryLevel());
        } catch (Exception e) {
            return ResponsePayloadUtility.createFailedResponse("drone not found");
        }
        return ResponsePayloadUtility.createSuccessResponse(droneBatteryLevelResponse, Constants.ResponseMessages.SUCCESS);
    }

    @Override
    public void checkDroneBatteryLevelsSchedule() {
        List<Drone> availableDrones = droneRepository.findAll();
        if (!ObjectUtils.isEmpty(availableDrones)) {
            for (Drone drone : availableDrones) {
                droneBatteryLevelAuditService.auditDroneBatteryLevel(drone.getId(), drone.getBatteryLevel());
                log.info("Battery Level scheduler: serial number: {}, batery level {}, time {}", drone.getSerialNumber(), drone.getBatteryLevel(), LocalDateTime.now());
            }
        } else {
            log.info("Battery Level scheduler: No available drones found");
        }
    }
}
