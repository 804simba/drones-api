package com.musalasoft.dronesapi.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medications_tbl")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long id;

    @Column(name = "medication_name")
    private String name;

    @Column(name = "medication_weight")
    private Double weight;

    @Column(name = "medication_code")
    private String code;

    @Column(name = "medication_image_url")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone drone;
}
