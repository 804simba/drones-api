package com.musalasoft.dronesapi.rest.service;

public interface DroneBatteryLevelAuditService {
    void auditDroneBatteryLevel(Long droneId, double batteryLevel);
}
