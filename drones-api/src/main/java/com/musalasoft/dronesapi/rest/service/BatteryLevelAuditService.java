package com.musalasoft.dronesapi.rest.service;

public interface BatteryLevelAuditService {
    void auditBatteryLevelCheck(Long droneId, double batteryLevel);
}
