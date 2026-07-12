package com.hospital.management.repository;

import com.hospital.management.domain.entity.InsuranceClaim;
import com.hospital.management.domain.enums.InsuranceClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, UUID> {
    List<InsuranceClaim> findByClinicId(UUID clinicId);
    List<InsuranceClaim> findByClinicIdAndStatus(UUID clinicId, InsuranceClaimStatus status);
}
