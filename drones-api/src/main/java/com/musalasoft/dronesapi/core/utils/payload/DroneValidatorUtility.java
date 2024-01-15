package com.musalasoft.dronesapi.core.utils.payload;

import com.musalasoft.dronesapi.core.exception.DroneBatteryDischargedException;
import com.musalasoft.dronesapi.core.exception.DroneOverLoadException;
import com.musalasoft.dronesapi.model.entity.Drone;
import com.musalasoft.dronesapi.model.entity.Medication;
import org.springframework.util.ObjectUtils;

import java.util.Set;

public class DroneValidatorUtility {
    public static void validateDroneWeightLimit(Drone drone, double medicationWeight) {
        double totalDroneMedicationsWeight = calculateTotalDroneWeight(drone);
        double updatedDroneWeight = totalDroneMedicationsWeight + medicationWeight;
        if (updatedDroneWeight > drone.getWeightLimit()) {
            throw new DroneOverLoadException("cannot load drone weight limit exceeded");
        }
    }

    private static double calculateTotalDroneWeight(Drone drone) {
        Set<Medication> medications = drone.getMedications();
        return !ObjectUtils.isEmpty(medications) ? medications.stream().mapToDouble(Medication::getWeight).sum() : 0.0;
    }

    public static void validateDroneBatteryLevel(Drone drone) {
        if (drone.getBatteryLevel() < 25) {
            throw new DroneBatteryDischargedException("drone cannot be in LOADING state if battery level is below 25%");
        }
    }
}
