package com.musalasoft.dronesapi.model.repository;

import com.musalasoft.dronesapi.model.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
