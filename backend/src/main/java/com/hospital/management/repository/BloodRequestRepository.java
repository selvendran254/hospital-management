package com.hospital.management.repository;

import com.hospital.management.domain.entity.BloodRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BloodRequestRepository extends JpaRepository<BloodRequest, UUID> {

    List<BloodRequest> findByStatus(String status);

    List<BloodRequest> findByPatientId(UUID patientId);
}
