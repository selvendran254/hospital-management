package com.hospital.management.repository;

import com.hospital.management.domain.entity.RecurringAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecurringAppointmentRepository extends JpaRepository<RecurringAppointment, UUID> {
    List<RecurringAppointment> findByDoctorIdAndActiveTrue(UUID doctorId);
}
