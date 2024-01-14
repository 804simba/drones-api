package com.musalasoft.dronesapi.model.repository;

import com.musalasoft.dronesapi.model.entity.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    boolean existsBySerialNumber(String serialNumber);

    boolean existsBySerialNumberAndId(String serialNumber, Long droneId);
    @Query("SELECT d FROM Drone d LEFT JOIN FETCH d.medications m WHERE d.id = :droneId")
    Optional<Drone> findMedicationsByDroneId(@Param("droneId") Long droneId);

}
