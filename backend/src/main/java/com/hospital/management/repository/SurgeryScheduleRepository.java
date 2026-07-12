package com.hospital.management.repository;

import com.hospital.management.domain.entity.SurgerySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SurgeryScheduleRepository extends JpaRepository<SurgerySchedule, UUID> {
    List<SurgerySchedule> findByClinicIdAndSurgeryDate(UUID clinicId, LocalDate surgeryDate);
}
