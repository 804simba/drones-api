package com.musalasoft.dronesapi.rest.controller;

import com.musalasoft.dronesapi.model.payload.request.LoadDroneRequest;
import com.musalasoft.dronesapi.model.payload.request.RegisterDroneRequest;
import com.musalasoft.dronesapi.model.payload.response.BaseResponse;
import com.musalasoft.dronesapi.rest.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/drones")
public class DispatchController {
    @Autowired
    private DroneService droneService;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> registerDrone(@RequestBody RegisterDroneRequest registerDroneRequest) {
        return droneService.registerDrone(registerDroneRequest);
    }

    @PostMapping(value = "/{droneId}/load", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> loadDrone(@PathVariable Long droneId, @RequestBody LoadDroneRequest loadDroneRequest) {
        return droneService.loadDrone(droneId, loadDroneRequest);
    }

    @GetMapping(value = "/{droneId}/loaded-medications", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> getLoadedMedications(@PathVariable Long droneId) {
        return droneService.getLoadedMedication(droneId);
    }

    @GetMapping(value = "/available-for-loading", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> getAvailableDronesForLoading(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                                                                           @RequestParam(name = "pageSize", defaultValue = "30") int pageSize) {
        return droneService.getAvailableDronesForLoading(pageNumber, pageSize);
    }

    @GetMapping(value = "/{droneId}/battery-level", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> getDroneBatteryLevel(@PathVariable Long droneId) {
        return droneService.getDroneBatteryLevel(droneId);
    }
}
