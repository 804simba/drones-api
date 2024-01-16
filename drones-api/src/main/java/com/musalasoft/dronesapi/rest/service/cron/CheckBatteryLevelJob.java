package com.musalasoft.dronesapi.rest.service.cron;

import com.musalasoft.dronesapi.rest.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class CheckBatteryLevelJob {
    private final DroneService droneService;

    @Autowired
    public CheckBatteryLevelJob(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(fixedRate = 120000)
    public void checkBatteryLevelScheduler() {
        droneService.checkDroneBatteryLevelsSchedule();
    }
}
