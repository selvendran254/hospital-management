package com.hospital.management.repository;

import com.hospital.management.domain.entity.LabReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabReportRepository extends JpaRepository<LabReport, UUID> {

    Page<LabReport> findByPatientId(UUID patientId, Pageable pageable);

    List<LabReport> findByCompletedFalse();

    Page<LabReport> findByCompleted(Boolean completed, Pageable pageable);
}
