package com.hospital.management.repository;

import com.hospital.management.domain.entity.StaffAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface StaffAttendanceRepository extends JpaRepository<StaffAttendance, UUID> {
    List<StaffAttendance> findByClinicIdAndStaffId(UUID clinicId, UUID staffId);
    List<StaffAttendance> findByClinicIdAndCheckInBetween(UUID clinicId, Instant from, Instant to);
}
