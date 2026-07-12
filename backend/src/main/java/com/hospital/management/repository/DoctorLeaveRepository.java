package com.hospital.management.repository;

import com.hospital.management.domain.entity.DoctorLeave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorLeaveRepository extends JpaRepository<DoctorLeave, UUID> {

    List<DoctorLeave> findByDoctorId(UUID doctorId);

    List<DoctorLeave> findByDoctorIdAndEndDateGreaterThanEqual(UUID doctorId, LocalDate date);

    Page<DoctorLeave> findByDoctorId(UUID doctorId, Pageable pageable);

    long countByDoctorIdAndEndDateGreaterThanEqual(UUID doctorId, LocalDate date);
}
