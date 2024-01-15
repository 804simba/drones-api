package com.musalasoft.dronesapi.core.utils.payload;

import com.musalasoft.dronesapi.model.enums.DroneModel;
import com.musalasoft.dronesapi.model.enums.DroneState;

public class DroneModelMapper {
    public static DroneModel mapModel(String model) {
        switch (model.toLowerCase()) {
            case "lightweight":
                return DroneModel.LIGHTWEIGHT;
            case "middleweight":
                return DroneModel.MIDDLEWEIGHT;
            case "cruiserweight":
                return DroneModel.CRUISERWEIGHT;
            case "heavyweight":
                return DroneModel.HEAVYWEIGHT;
            default:
                throw new IllegalArgumentException("invalid drone model: " + model);
        }
    }
}
