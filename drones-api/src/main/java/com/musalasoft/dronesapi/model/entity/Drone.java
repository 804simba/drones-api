package com.musalasoft.dronesapi.model.entity;

import com.musalasoft.dronesapi.model.enums.DroneState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(
        name = "drones_tbl",
        indexes = { @Index(name = "serial_number_idx", columnList = "serial_number" )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Drone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drone_id")
    private Long id;

    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "weight_limit")
    private double weightLimit;

    @Column(name = "battery_level")
    private double batteryLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "drone_state")
    private DroneState droneState;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Medication> medications;
}
