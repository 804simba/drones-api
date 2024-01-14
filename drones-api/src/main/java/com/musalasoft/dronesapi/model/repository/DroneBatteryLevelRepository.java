package com.musalasoft.dronesapi.model.repository;

import com.musalasoft.dronesapi.model.entity.DroneBatteryLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneBatteryLevelRepository extends JpaRepository<DroneBatteryLevel, Long> {
}
