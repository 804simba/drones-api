package com.musalasoft.dronesapi.rest.service;

import com.musalasoft.dronesapi.model.payload.drone.LoadDroneRequest;
import com.musalasoft.dronesapi.model.payload.drone.RegisterDroneRequest;
import com.musalasoft.dronesapi.model.payload.base.BaseResponse;

public interface DroneService {
    BaseResponse<?> registerDrone(RegisterDroneRequest registerDroneRequest);

    BaseResponse<?> loadDrone(Long droneId, LoadDroneRequest loadDroneRequest);

    BaseResponse<?> getLoadedMedication(Long droneId);

    BaseResponse<?> getAvailableDronesForLoading(int pageNumber, int pageSize);

    BaseResponse<?> getDroneBatteryLevel(Long droneId);

    void checkDroneBatteryLevelsSchedule();
}
