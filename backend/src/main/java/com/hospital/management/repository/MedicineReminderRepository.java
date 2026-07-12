package com.hospital.management.repository;

import com.hospital.management.domain.entity.MedicineReminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicineReminderRepository extends JpaRepository<MedicineReminder, UUID> {
    List<MedicineReminder> findByPatientIdAndActiveTrue(UUID patientId);
}
