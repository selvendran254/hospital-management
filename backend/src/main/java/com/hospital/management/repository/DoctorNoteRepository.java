package com.hospital.management.repository;

import com.hospital.management.domain.entity.DoctorNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DoctorNoteRepository extends JpaRepository<DoctorNote, UUID> {
    List<DoctorNote> findByPatientIdOrderByCreatedAtDesc(UUID patientId);
}
