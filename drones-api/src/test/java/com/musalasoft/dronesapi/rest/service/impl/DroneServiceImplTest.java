package com.musalasoft.dronesapi.rest.service.impl;

import com.musalasoft.dronesapi.core.exception.DroneAlreadyExistsException;
import com.musalasoft.dronesapi.core.exception.DroneNotFoundException;
import com.musalasoft.dronesapi.core.exception.DroneOverLoadException;
import com.musalasoft.dronesapi.core.utils.Constants;
import com.musalasoft.dronesapi.core.utils.payload.DroneModelMapper;
import com.musalasoft.dronesapi.model.entity.Drone;
import com.musalasoft.dronesapi.model.entity.Medication;
import com.musalasoft.dronesapi.model.enums.DroneModel;
import com.musalasoft.dronesapi.model.enums.DroneState;
import com.musalasoft.dronesapi.model.payload.drone.DroneBatteryLevelResponse;
import com.musalasoft.dronesapi.model.payload.drone.DroneDto;
import com.musalasoft.dronesapi.model.payload.drone.LoadDroneRequest;
import com.musalasoft.dronesapi.model.payload.drone.RegisterDroneRequest;
import com.musalasoft.dronesapi.model.payload.base.BaseResponse;
import com.musalasoft.dronesapi.model.payload.medication.FetchLoadedMedicationsResponse;
import com.musalasoft.dronesapi.model.payload.medication.MedicationDto;
import com.musalasoft.dronesapi.model.payload.pagination.PagedResponse;
import com.musalasoft.dronesapi.model.repository.DroneRepository;
import com.musalasoft.dronesapi.model.repository.MediaRepository;
import com.musalasoft.dronesapi.model.repository.MedicationRepository;
import com.musalasoft.dronesapi.rest.service.DroneBatteryLevelAuditService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceImplTest {
    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DroneBatteryLevelAuditService droneBatteryLevelAuditService;

    private static Validator validator;

    @InjectMocks
    private DroneServiceImpl droneService;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldTestRegisterDroneSuccessfullyRegistersWithCorrectParameters() {
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber("ABC123")
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(300.0)
                .batteryCapacity(80)
                .build();

        when(droneRepository.existsBySerialNumber(request.getSerialNumber())).thenReturn(false);

        Drone savedDrone = new Drone();
        savedDrone.setId(1L);
        savedDrone.setSerialNumber(request.getSerialNumber());
        savedDrone.setBatteryLevel(request.getBatteryCapacity());
        savedDrone.setWeightLimit(request.getWeightLimit());
        savedDrone.setDroneState(DroneState.IDLE);
        savedDrone.setModel(DroneModelMapper.mapModel(request.getModel()));

        when(droneRepository.save(any(Drone.class))).thenReturn(savedDrone);

        var result = droneService.registerDrone(request);

        verify(droneRepository, times(1)).existsBySerialNumber(request.getSerialNumber());
        verify(droneRepository, times(1)).save(any(Drone.class));

        DroneDto droneDto = extractBaseResponseData(result, DroneDto.class);
        assertNotNull(droneDto);
        assertEquals(savedDrone.getId(), droneDto.getId());
        assertEquals(savedDrone.getSerialNumber(), droneDto.getSerialNumber());
        assertEquals(savedDrone.getBatteryLevel(), droneDto.getBatteryLevel());
        assertEquals(savedDrone.getWeightLimit(), droneDto.getWeightLimit());
        assertEquals(savedDrone.getDroneState().name(), droneDto.getState());

        assertEquals(HttpStatus.OK.value(), result.getResponseCode());
        assertEquals("drone registered successfully", result.getResponseMessage());
    }

    @Test
    void shouldTestRegisterDroneThrowsDroneAlreadyExistsException() {
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber("ABC123")
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(300.0)
                .batteryCapacity(80)
                .build();

        when(droneRepository.existsBySerialNumber(request.getSerialNumber())).thenReturn(true);

        verify(droneRepository, never()).save(any(Drone.class));

        assertThrows(DroneAlreadyExistsException.class, () -> droneService.registerDrone(request));
    }

    @Test
    void shouldTestRegisterDroneValidationOfMoreThanHundredCharactersSerialNumberFails() {
        String serialNumberMoreThan100 = "gdhdhdhdhdhshhhshshsgdhdhdhdhdhshhhshshsgdhdhdhdhdhshhhshshsgdhdhdhdhdhshhhshshsgdhdhdhdhdhshhhshshsretaaa";
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber(serialNumberMoreThan100)
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(300.0)
                .batteryCapacity(80)
                .build();

        Set<ConstraintViolation<RegisterDroneRequest>> violations = validator.validate(request);

        assertTrue(violations.size() > 0, "Validation should produce violations");
        assertEquals("Serial number must be at most 100 characters", violations.iterator().next().getMessage());
    }

    @Test
    void shouldTestRegisterDroneValidationOfLessThanHundredCharactersSerialNumberIsSuccessful() {
        String serialNumberLessThan100 = "gdhdhdhdhdhshhhshshsgdhdhdhdhdhshhhshshsgdhdhdhdhdhshhhsh";
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber(serialNumberLessThan100)
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(300.0)
                .batteryCapacity(80)
                .build();

        Set<ConstraintViolation<RegisterDroneRequest>> violations = validator.validate(request);

        assertFalse(violations.size() > 0, "Validation should produce violations");
    }

    @Test
    void shouldTestRegisterDroneValidationOfWeightGreaterThan500Fails() {
        String serialNumberLessThan100 = "loremipsum";
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber(serialNumberLessThan100)
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(501.0)
                .batteryCapacity(80)
                .build();

        Set<ConstraintViolation<RegisterDroneRequest>> violations = validator.validate(request);

        assertTrue(violations.size() > 0, "Validation should produce violations");
    }

    @Test
    void shouldTestRegisterDroneValidationOfWeightGreaterThan500IsSuccessful() {
        String serialNumberLessThan100 = "loremipsum";
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber(serialNumberLessThan100)
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(500.0)
                .batteryCapacity(80)
                .build();

        Set<ConstraintViolation<RegisterDroneRequest>> violations = validator.validate(request);

        assertFalse(violations.size() > 0, "Validation should produce violations");
    }

    @Test
    void shouldTestRegisterDroneValidationOfBatteryPercentageLessThan100IsSuccessful() {
        String serialNumberLessThan100 = "loremipsum";
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber(serialNumberLessThan100)
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(500.0)
                .batteryCapacity(100)
                .build();

        Set<ConstraintViolation<RegisterDroneRequest>> violations = validator.validate(request);

        assertFalse(violations.size() > 0, "Validation should produce violations");
    }

    @Test
    void  shouldTestRegisterDroneValidationOfBatteryPercentageGreaterThan100Fails() {
        String serialNumberLessThan100 = "loremipsum";
        RegisterDroneRequest request = RegisterDroneRequest.builder()
                .serialNumber(serialNumberLessThan100)
                .model(DroneModel.LIGHTWEIGHT.name())
                .weightLimit(500.0)
                .batteryCapacity(101)
                .build();

        Set<ConstraintViolation<RegisterDroneRequest>> violations = validator.validate(request);

        assertTrue(violations.size() > 0, "Validation should produce violations");
    }

    @Test
    void shouldTestLoadDroneValidationOfInvalidMedicationNameCharacterFails() {
        String invalidName = "strepsils!!*#$";
        LoadDroneRequest request = LoadDroneRequest.builder()
                .droneSerialNumber("lorem")
                .medicationImageId(1L)
                .medicationWeight(30.0)
                .medicationCode("MED_2022")
                .medicationName(invalidName)
                .build();

        Set<ConstraintViolation<LoadDroneRequest>> violations = validator.validate(request);

        assertTrue(violations.size() > 0, "Validation should produce violations");
        assertEquals("medication name must only contain letters, numbers, '-', or '_'", violations.iterator().next().getMessage());
    }

    @Test
    void shouldTestLoadDroneValidationOfValidMedicationNamePasses() {
        String validName = "Aspirin";
        LoadDroneRequest request = LoadDroneRequest.builder()
                .droneSerialNumber("lorem")
                .medicationImageId(1L)
                .medicationWeight(30.0)
                .medicationCode("MED_2022")
                .medicationName(validName)
                .build();

        Set<ConstraintViolation<LoadDroneRequest>> violations = validator.validate(request);

        assertEquals(0, violations.size(), "Validation should not produce violations");
    }

    @Test
    void shouldTestLoadDroneValidationOfInvalidMedicationCodeCharacterFails() {
        String invalidCode = "strepsils!!*#$";
        LoadDroneRequest request = LoadDroneRequest.builder()
                .droneSerialNumber("lorem")
                .medicationImageId(1L)
                .medicationWeight(30.0)
                .medicationCode(invalidCode)
                .medicationName("lorem")
                .build();

        Set<ConstraintViolation<LoadDroneRequest>> violations = validator.validate(request);

        assertTrue(violations.size() > 0, "Validation should produce violations");
        assertEquals("medication code must only contain upper case letters, numbers, or '_'", violations.iterator().next().getMessage());
    }

    @Test
    void shouldTestLoadDroneSuccessfullyRegistersWithMedicationsUnderDroneWeightLimit() {
        LoadDroneRequest request = LoadDroneRequest.builder().droneSerialNumber("loremipsum")
                .medicationCode("abc123").medicationName("strepsils").medicationWeight(200.0)
                .medicationImageId(1L).build();

        Long droneId = 1L;

        Drone savedDrone = new Drone();
        savedDrone.setId(1L);
        savedDrone.setSerialNumber("loremipsum");
        savedDrone.setBatteryLevel(100.0);
        savedDrone.setWeightLimit(400.0);
        savedDrone.setDroneState(DroneState.LOADED);
        savedDrone.setModel(DroneModel.HEAVYWEIGHT);

        Medication savedMedication = new Medication();
        savedMedication.setCode(request.getMedicationCode());
        savedMedication.setName(request.getMedicationName());
        savedMedication.setId(1L);
        savedMedication.setWeight(100.0);
        savedMedication.setDrone(savedDrone);
        savedMedication.setImageUrl(Constants.Media.PLACEHOLDER_URL);

        savedDrone.setMedications(Set.of(savedMedication));

        when(droneRepository.existsBySerialNumberAndId(request.getDroneSerialNumber(), droneId)).thenReturn(true);
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(savedDrone));
        when(mediaRepository.findById(request.getMedicationImageId())).thenReturn(Optional.empty());

        when(medicationRepository.save(any(Medication.class))).thenReturn(savedMedication);
        when(droneRepository.save(any(Drone.class))).thenReturn(savedDrone);

        var result = droneService.loadDrone(droneId, request);

        verify(droneRepository, times(1)).existsBySerialNumberAndId(request.getDroneSerialNumber(), droneId);
        verify(droneRepository, times(1)).save(any(Drone.class));

        DroneDto droneDto = extractBaseResponseData(result, DroneDto.class);
        assertNotNull(droneDto);
        assertEquals(savedDrone.getId(), droneDto.getId());
        assertEquals(savedDrone.getSerialNumber(), droneDto.getSerialNumber());
        assertEquals(savedDrone.getBatteryLevel(), droneDto.getBatteryLevel());
        assertEquals(savedDrone.getWeightLimit(), droneDto.getWeightLimit());
        assertEquals(savedDrone.getDroneState().name(), droneDto.getState());
        assertEquals(savedDrone.getMedications().stream().findFirst().get().getName(), savedMedication.getName());

        assertEquals(HttpStatus.OK.value(), result.getResponseCode());
        assertEquals("drone loaded successfully", result.getResponseMessage());
    }

    @Test
    void shouldTestLoadDroneFailsForMedicationOverDroneWeightLimit() {
        LoadDroneRequest request = LoadDroneRequest.builder().droneSerialNumber("loremipsum")
                .medicationCode("abc123").medicationName("strepsils").medicationWeight(500.0)
                .medicationImageId(1L).build();

        Long droneId = 1L;

        Drone savedDrone = new Drone();
        savedDrone.setId(1L);
        savedDrone.setSerialNumber("loremipsum");
        savedDrone.setBatteryLevel(100.0);
        savedDrone.setWeightLimit(400.0);
        savedDrone.setDroneState(DroneState.LOADED);
        savedDrone.setModel(DroneModel.HEAVYWEIGHT);

        Medication savedMedication = new Medication();
        savedMedication.setCode(request.getMedicationCode());
        savedMedication.setName(request.getMedicationName());
        savedMedication.setId(1L);
        savedMedication.setWeight(100.0);
        savedMedication.setDrone(savedDrone);
        savedMedication.setImageUrl(Constants.Media.PLACEHOLDER_URL);

        savedDrone.setMedications(Set.of(savedMedication));

        when(droneRepository.existsBySerialNumberAndId(request.getDroneSerialNumber(), droneId)).thenReturn(true);
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(savedDrone));

        assertThrows(DroneOverLoadException.class, () -> droneService.loadDrone(droneId, request));
    }

    @Test
    void shouldTestLoadDroneFailsForMedicationUnderDroneWeightLimit() {
        LoadDroneRequest request = LoadDroneRequest.builder().droneSerialNumber("loremipsum")
                .medicationCode("abc123").medicationName("strepsils").medicationWeight(100.0)
                .medicationImageId(1L).build();

        Long droneId = 1L;

        Drone savedDrone = new Drone();
        savedDrone.setId(1L);
        savedDrone.setSerialNumber("loremipsum");
        savedDrone.setBatteryLevel(100.0);
        savedDrone.setWeightLimit(400.0);
        savedDrone.setDroneState(DroneState.IDLE);
        savedDrone.setModel(DroneModel.HEAVYWEIGHT);

        Medication savedMedication = new Medication();
        savedMedication.setCode(request.getMedicationCode());
        savedMedication.setName(request.getMedicationName());
        savedMedication.setId(1L);
        savedMedication.setWeight(100.0);
        savedMedication.setDrone(savedDrone);
        savedMedication.setImageUrl(Constants.Media.PLACEHOLDER_URL);

        savedDrone.setMedications(Set.of(savedMedication));

        when(droneRepository.existsBySerialNumberAndId(request.getDroneSerialNumber(), droneId)).thenReturn(true);
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(savedDrone));
        when(droneRepository.save(any(Drone.class))).thenReturn(savedDrone);
        when(mediaRepository.findById(request.getMedicationImageId())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> droneService.loadDrone(droneId, request));
    }

    @Test
    void shouldTestLoadDroneFailsForDroneNotFoundThrowsException() {
        LoadDroneRequest request = LoadDroneRequest.builder().droneSerialNumber("loremipsum")
                .medicationCode("abc123").medicationName("strepsils").medicationWeight(500.0)
                .medicationImageId(1L).build();
        Long droneId = 1L;

        when(droneRepository.existsBySerialNumberAndId(request.getDroneSerialNumber(), droneId)).thenReturn(false);

        assertThrows(DroneNotFoundException.class, () -> droneService.loadDrone(droneId, request));
    }

    @Test
    void shouldTestGetLoadedMedicationsSuccessfulFetch() {
        Long droneId = 1L;
        Drone drone = new Drone();
        drone.setId(droneId);
        drone.setSerialNumber("ABC123");
        drone.setMedications(new HashSet<>());
        when(droneRepository.findMedicationsByDroneId(droneId)).thenReturn(Optional.of(drone));

        var result = droneService.getLoadedMedication(droneId);


        assertEquals(Constants.ResponseStatusCode.SUCCESS, result.getResponseCode());
        assertEquals("fetched medications records successfully for drone with id " + droneId, result.getResponseMessage());

        FetchLoadedMedicationsResponse responseData = extractBaseResponseData(result, FetchLoadedMedicationsResponse.class);
        assertNotNull(responseData);
        assertEquals(drone.getId(), responseData.getDroneId());
        assertEquals(drone.getSerialNumber(), responseData.getDroneSerialNumber());
        assertEquals(drone.getMedications().size(), responseData.getMedications().size());
    }

    @Test
    void shouldTestGetLoadedMedicationsWhenDroneNotFound() {
        Long droneId = 1L;
        when(droneRepository.findMedicationsByDroneId(droneId)).thenReturn(Optional.empty());
        assertThrows(DroneNotFoundException.class, () -> droneService.getLoadedMedication(droneId));
    }

    @Test
    void shouldTestGetLoadedMedicationsShouldReturnValidResponse() {
        Long droneId = 1L;
        Drone mockDrone = createMockDrone();
        when(droneRepository.findMedicationsByDroneId(droneId)).thenReturn(Optional.of(mockDrone));

        for (Medication mockMedication : mockDrone.getMedications()) {
            when(mockMedication.getId()).thenReturn(1L);
            when(mockMedication.getWeight()).thenReturn(30.0);
            when(mockMedication.getName()).thenReturn("Mock Medication");
            when(mockMedication.getImageUrl()).thenReturn("mock-image-url");
            when(mockMedication.getCode()).thenReturn("ABC123");
        }

        BaseResponse<?> result = droneService.getLoadedMedication(droneId);

        assertNotNull(result);
        assertEquals(Constants.ResponseStatusCode.SUCCESS, result.getResponseCode());
        assertEquals("fetched medications records successfully for drone with id " + droneId, result.getResponseMessage());

        FetchLoadedMedicationsResponse responseData = extractBaseResponseData(result, FetchLoadedMedicationsResponse.class);
        assertNotNull(responseData);
        assertEquals(mockDrone.getId(), responseData.getDroneId());
        assertEquals(mockDrone.getSerialNumber(), responseData.getDroneSerialNumber());

        assertEquals(mockDrone.getMedications().size(), responseData.getMedications().size());

        Iterator<Medication> mockMedicationIterator = mockDrone.getMedications().iterator();
        for (MedicationDto actualDto : responseData.getMedications()) {
            assertTrue(mockMedicationIterator.hasNext());
            Medication mockMedication = mockMedicationIterator.next();

            assertEquals(mockMedication.getId(), actualDto.getId());
            assertEquals(mockMedication.getWeight(), actualDto.getWeight());
            assertEquals(mockMedication.getName(), actualDto.getName());
            assertEquals(mockMedication.getImageUrl(), actualDto.getImage());
            assertEquals(mockMedication.getCode(), actualDto.getCode());
        }

        verify(droneRepository, times(1)).findMedicationsByDroneId(droneId);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void getAvailableDronesForLoadingThrowsExceptionForDroneNotFound() {
        int pageNumber = 0;
        int pageSize = 10;
        when(droneRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        assertThrows(DroneNotFoundException.class, () -> droneService.getAvailableDronesForLoading(pageNumber, pageSize));
    }

    @Test
    void getAvailableDronesForLoadingShouldReturnValidResponse() {
        int pageNumber = 0;
        int pageSize = 10;

        Drone mockDrone = createMockDrone();
        Page<Drone> mockPage = new PageImpl<>(Collections.singletonList(mockDrone));

        when(droneRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        BaseResponse<?> result = droneService.getAvailableDronesForLoading(pageNumber, pageSize);

        assertNotNull(result);
        assertEquals(Constants.ResponseStatusCode.SUCCESS, result.getResponseCode());
        assertEquals("fetched available drones successfully", result.getResponseMessage());

        PagedResponse<?> responseData = extractBaseResponseData(result, PagedResponse.class);
        assertNotNull(responseData);
        assertEquals(1, responseData.getData().getContent().size());

        DroneDto droneDto = (DroneDto) responseData.getData().getContent().get(0);
        assertEquals(mockDrone.getId(), droneDto.getId());
        assertEquals(mockDrone.getSerialNumber(), droneDto.getSerialNumber());
        verify(droneRepository, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void shouldTestGetDroneBatteryLevelShouldReturnSuccessResponse() {
        Long droneId = 1L;
        Drone mockDrone = createMockDrone();
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(mockDrone));

        // Act
        BaseResponse<?> result = droneService.getDroneBatteryLevel(droneId);

        // Assert
        assertNotNull(result);
        assertEquals(Constants.ResponseStatusCode.SUCCESS, result.getResponseCode());
        assertEquals(Constants.ResponseMessages.SUCCESS, result.getResponseMessage());

        DroneBatteryLevelResponse responseData = extractBaseResponseData(result, DroneBatteryLevelResponse.class);
        assertNotNull(responseData);
        assertEquals(mockDrone.getSerialNumber(), responseData.getDroneSerialNumber());
        assertEquals(mockDrone.getBatteryLevel(), responseData.getBatteryLevel());

        verify(droneRepository, times(1)).findById(droneId);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void shouldTestGetDroneBatteryLevelShouldReturnFailedResponseWhenDroneNotFound() {
        Long droneId = 1L;
        when(droneRepository.findById(droneId)).thenReturn(Optional.empty());

        BaseResponse<?> result = droneService.getDroneBatteryLevel(droneId);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponseCode());
        assertEquals("drone not found", result.getResponseMessage());

        verify(droneRepository, times(1)).findById(droneId);
        verifyNoMoreInteractions(droneRepository);
    }

    @Test
    void shouldTestCheckDroneBatteryLevelsSchedulerShouldAuditBatteryLevelForAvailableDrones() {
        List<Drone> mockDrones = Arrays.asList(createMockDrone(), createMockDrone());
        when(droneRepository.findAll()).thenReturn(mockDrones);

        droneService.checkDroneBatteryLevelsSchedule();

        verify(droneBatteryLevelAuditService, times(mockDrones.size())).auditDroneBatteryLevel(anyLong(), anyDouble());
        verify(droneRepository, times(1)).findAll();
        verifyNoMoreInteractions(droneBatteryLevelAuditService, droneRepository);
    }

    @Test
    void shouldTestCheckDroneBatteryLevelsSchedulerShouldLogInfoWhenNoAvailableDronesFound() {
        when(droneRepository.findAll()).thenReturn(Collections.emptyList());
        droneService.checkDroneBatteryLevelsSchedule();
        verify(droneRepository, times(1)).findAll();
        verifyNoMoreInteractions(droneBatteryLevelAuditService, droneRepository);
    }

    private Drone createMockDrone() {
        Drone drone = new Drone();
        drone.setId(1L);
        drone.setModel(DroneModel.HEAVYWEIGHT);
        drone.setBatteryLevel(100.0);
        drone.setWeightLimit(800.0);
        drone.setSerialNumber("lorem");
        drone.setDroneState(DroneState.IDLE);
        drone.setMedications(Set.of(Mockito.mock(Medication.class)));
        return drone;
    }

    <T> T extractBaseResponseData(BaseResponse<?> response, Class<T> responseType) {
        return responseType.cast(response.getData());
    }
}