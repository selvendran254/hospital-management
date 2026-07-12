package com.hospital.management.repository;

import com.hospital.management.domain.entity.OperationTheatre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OperationTheatreRepository extends JpaRepository<OperationTheatre, UUID> {
    List<OperationTheatre> findByClinicId(UUID clinicId);
}
