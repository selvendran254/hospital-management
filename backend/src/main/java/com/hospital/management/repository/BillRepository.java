package com.hospital.management.repository;

import com.hospital.management.domain.entity.Bill;
import com.hospital.management.domain.enums.BillStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {

    Page<Bill> findByPatientId(UUID patientId, Pageable pageable);
    Page<Bill> findByClinicId(UUID clinicId, Pageable pageable);

    Page<Bill> findByStatus(BillStatus status, Pageable pageable);
}
