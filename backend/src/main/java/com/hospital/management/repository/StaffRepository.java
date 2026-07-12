package com.hospital.management.repository;

import com.hospital.management.domain.entity.Staff;
import com.hospital.management.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffRepository extends JpaRepository<Staff, UUID> {

    Optional<Staff> findByUserId(UUID userId);
    List<Staff> findByClinicId(UUID clinicId);

    List<Staff> findByRole(UserRole role);

    List<Staff> findByDepartmentId(UUID departmentId);

    List<Staff> findByActiveTrue();
}
