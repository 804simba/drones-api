package com.musalasoft.dronesapi.core.utils.payload;

import com.musalasoft.dronesapi.core.exception.DroneBatteryDischargedException;
import com.musalasoft.dronesapi.core.exception.DroneOverLoadException;
import com.musalasoft.dronesapi.model.entity.Drone;

public class DroneValidatorUtility {
    public static void validateDroneWeightLimit(Drone drone, double medicationWeight) {
        if (medicationWeight > drone.getWeightLimit()) {
            throw new DroneOverLoadException("drone weight limit exceeded");
        }
    }

    public static void validateDroneBatteryLevel(Drone drone) {
        if (drone.getBatteryLevel() < 25) {
            throw new DroneBatteryDischargedException("drone cannot be in LOADING state if battery level is below 25%");
        }
    }
}
