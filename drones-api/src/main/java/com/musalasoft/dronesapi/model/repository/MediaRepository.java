package com.musalasoft.dronesapi.model.repository;

import com.musalasoft.dronesapi.model.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
