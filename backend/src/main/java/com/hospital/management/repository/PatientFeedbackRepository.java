package com.hospital.management.repository;

import com.hospital.management.domain.entity.PatientFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PatientFeedbackRepository extends JpaRepository<PatientFeedback, UUID> {
    List<PatientFeedback> findByClinicId(UUID clinicId);
    List<PatientFeedback> findByClinicIdAndDoctorId(UUID clinicId, UUID doctorId);

    @Query("select coalesce(avg(pf.rating), 0) from PatientFeedback pf where pf.clinicId = :clinicId and pf.doctorId = :doctorId")
    Double averageRating(UUID clinicId, UUID doctorId);
}
