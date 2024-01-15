package com.musalasoft.dronesapi.rest.service.impl;

import com.musalasoft.dronesapi.core.exception.DroneNotFoundException;
import com.musalasoft.dronesapi.model.entity.Drone;
import com.musalasoft.dronesapi.model.entity.DroneBatteryLevel;
import com.musalasoft.dronesapi.model.repository.DroneBatteryLevelRepository;
import com.musalasoft.dronesapi.model.repository.DroneRepository;
import com.musalasoft.dronesapi.rest.service.BatteryLevelAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BatteryLevelAuditServiceImpl implements BatteryLevelAuditService {
    private final DroneRepository droneRepository;

    private final DroneBatteryLevelRepository droneBatteryLevelRepository;

    @Autowired
    public BatteryLevelAuditServiceImpl(DroneRepository droneRepository, DroneBatteryLevelRepository droneBatteryLevelRepository) {
        this.droneRepository = droneRepository;
        this.droneBatteryLevelRepository = droneBatteryLevelRepository;
    }

    @Override
    public void auditDroneBatteryLevel(Long droneId, double batteryLevel) {
        Drone foundDrone = droneRepository.findById(droneId)
                .orElseThrow(() -> new DroneNotFoundException("drone not found"));

        DroneBatteryLevel droneBatteryLevel = new DroneBatteryLevel();
        droneBatteryLevel.setId(foundDrone.getId());
        droneBatteryLevel.setBatteryLevel(foundDrone.getBatteryLevel());
        droneBatteryLevelRepository.save(droneBatteryLevel);
        log.info("[ Battery level check ] -- Drone id: " + foundDrone.getId() + " -- Battery level: " + foundDrone.getBatteryLevel());
    }
}
