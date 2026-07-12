package com.hospital.management.repository;

import com.hospital.management.domain.entity.ConsentRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsentRecordRepository extends JpaRepository<ConsentRecord, UUID> {
    List<ConsentRecord> findByClinicIdAndPatientId(UUID clinicId, UUID patientId);
}
