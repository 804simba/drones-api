package com.musalasoft.dronesapi.rest.service;

import com.musalasoft.dronesapi.model.payload.request.LoadDroneRequest;
import com.musalasoft.dronesapi.model.payload.request.RegisterDroneRequest;
import com.musalasoft.dronesapi.model.payload.response.BaseResponse;

public interface DroneService {
    BaseResponse<?> registerDrone(RegisterDroneRequest registerDroneRequest);

    BaseResponse<?> loadDrone(Long droneId, LoadDroneRequest loadDroneRequest);

    BaseResponse<?> getLoadedMedication(Long droneId);

    BaseResponse<?> getAvailableDronesForLoading(int pageNumber, int pageSize);

    BaseResponse<?> getDroneBatteryLevel(Long droneId);

    void checkBatteryLevelsSchedule();
}
