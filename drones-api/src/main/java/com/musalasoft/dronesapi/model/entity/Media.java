package com.musalasoft.dronesapi.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "media_tbl")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Media extends BaseEntity{

    @Column(name = "media_url")
    private String url;

    @Column(name = "media_filename")
    private String fileName;

    @Column(name = "cloudinary_public_id")
    private String publicId;

    @Column(name = "media_type")
    private String mediaType;

    @CreationTimestamp
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
