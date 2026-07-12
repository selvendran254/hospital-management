package com.hospital.management.repository;

import com.hospital.management.domain.entity.Admission;
import com.hospital.management.domain.enums.AdmissionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, UUID> {

    Page<Admission> findByPatientId(UUID patientId, Pageable pageable);

    List<Admission> findByStatus(AdmissionStatus status);

    Optional<Admission> findByBedIdAndStatus(UUID bedId, AdmissionStatus status);
}
