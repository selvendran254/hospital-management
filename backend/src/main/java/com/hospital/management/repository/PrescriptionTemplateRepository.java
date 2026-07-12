package com.hospital.management.repository;

import com.hospital.management.domain.entity.PrescriptionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrescriptionTemplateRepository extends JpaRepository<PrescriptionTemplate, UUID> {
    List<PrescriptionTemplate> findByDoctorId(UUID doctorId);
}
