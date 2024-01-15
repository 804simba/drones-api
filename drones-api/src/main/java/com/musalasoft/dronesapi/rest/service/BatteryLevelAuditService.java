package com.musalasoft.dronesapi.rest.service;

public interface BatteryLevelAuditService {
    void auditDroneBatteryLevel(Long droneId, double batteryLevel);
}
