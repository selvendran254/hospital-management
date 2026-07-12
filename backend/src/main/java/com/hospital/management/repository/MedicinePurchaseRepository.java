package com.hospital.management.repository;

import com.hospital.management.domain.entity.MedicinePurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MedicinePurchaseRepository extends JpaRepository<MedicinePurchase, UUID> {

    Page<MedicinePurchase> findByMedicineId(UUID medicineId, Pageable pageable);
}
