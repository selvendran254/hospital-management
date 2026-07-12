package com.hospital.management.repository;

import com.hospital.management.domain.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    Page<Prescription> findByPatientId(UUID patientId, Pageable pageable);

    Page<Prescription> findByDoctorId(UUID doctorId, Pageable pageable);

    List<Prescription> findByAppointmentId(UUID appointmentId);
}
