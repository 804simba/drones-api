package com.musalasoft.dronesapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "drone_battery_level_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DroneBatteryLevel extends BaseEntity{

    @Column(name = "battery_level")
    private Double batteryLevel;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
