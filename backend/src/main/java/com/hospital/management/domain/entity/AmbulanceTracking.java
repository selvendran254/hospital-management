package com.hospital.management.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ambulance_tracking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmbulanceTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ambulance_id", nullable = false)
    private UUID ambulanceId;

    @Column(nullable = false, precision = 10)
    private Double latitude;

    @Column(nullable = false, precision = 10)
    private Double longitude;

    @Column(name = "status_message", length = 200)
    private String statusMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
