package com.hospital.management.repository;

import com.hospital.management.domain.entity.MedicineSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicineSaleRepository extends JpaRepository<MedicineSale, UUID> {

    Page<MedicineSale> findByMedicineId(UUID medicineId, Pageable pageable);

    Page<MedicineSale> findByPatientId(UUID patientId, Pageable pageable);
}
