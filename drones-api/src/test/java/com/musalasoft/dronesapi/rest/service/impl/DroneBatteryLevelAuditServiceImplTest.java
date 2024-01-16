package com.musalasoft.dronesapi.rest.service.impl;

import com.musalasoft.dronesapi.core.exception.DroneNotFoundException;
import com.musalasoft.dronesapi.model.entity.Drone;
import com.musalasoft.dronesapi.model.entity.DroneBatteryLevel;
import com.musalasoft.dronesapi.model.repository.DroneBatteryLevelRepository;
import com.musalasoft.dronesapi.model.repository.DroneRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneBatteryLevelAuditServiceImplTest {
    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneBatteryLevelRepository droneBatteryLevelRepository;

    @InjectMocks
    private DroneBatteryLevelAuditServiceImpl batteryLevelAuditService;

    @Test
    void shouldAuditDroneBatteryLevelWhenDroneIsFoundShouldSaveBatteryLevel() {
        Long droneId = 1L;
        double batteryLevel = 80.0;
        Drone foundDrone = new Drone();
        foundDrone.setId(droneId);
        foundDrone.setBatteryLevel(batteryLevel);

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(foundDrone));

        batteryLevelAuditService.auditDroneBatteryLevel(droneId, batteryLevel);

        verify(droneBatteryLevelRepository, times(1)).save(any(DroneBatteryLevel.class));
        verify(droneBatteryLevelRepository).save(argThat(droneBatteryLevel ->
                droneBatteryLevel.getId().equals(droneId) && droneBatteryLevel.getBatteryLevel() == batteryLevel));

        verifyNoMoreInteractions(droneBatteryLevelRepository);
    }

    @Test
    void shouldVerifyAuditDroneBatteryLevelWhenDroneNotFoundThrowsException() {
        Long droneId = 1L;
        double batteryLevel = 80.0;

        when(droneRepository.findById(droneId)).thenReturn(Optional.empty());

        assertThrows(DroneNotFoundException.class,
                () -> batteryLevelAuditService.auditDroneBatteryLevel(droneId, batteryLevel));
    }
}