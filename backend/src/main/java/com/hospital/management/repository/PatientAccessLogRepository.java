package com.hospital.management.repository;

import com.hospital.management.domain.entity.PatientAccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface PatientAccessLogRepository extends JpaRepository<PatientAccessLog, UUID> {
    List<PatientAccessLog> findByClinicIdAndPatientId(UUID clinicId, UUID patientId);
    List<PatientAccessLog> findByClinicIdAndAccessedAtBetween(UUID clinicId, Instant from, Instant to);
}
