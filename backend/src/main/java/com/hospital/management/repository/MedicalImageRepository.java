package com.hospital.management.repository;

import com.hospital.management.domain.entity.MedicalImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicalImageRepository extends JpaRepository<MedicalImage, UUID> {
    List<MedicalImage> findByClinicIdAndPatientId(UUID clinicId, UUID patientId);
}
