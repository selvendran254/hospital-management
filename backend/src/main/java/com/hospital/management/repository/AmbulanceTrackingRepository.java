package com.hospital.management.repository;

import com.hospital.management.domain.entity.AmbulanceTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AmbulanceTrackingRepository extends JpaRepository<AmbulanceTracking, UUID> {
    List<AmbulanceTracking> findByAmbulanceIdOrderByCreatedAtDesc(UUID ambulanceId);
}
