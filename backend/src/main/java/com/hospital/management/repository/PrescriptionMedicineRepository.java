package com.hospital.management.repository;

import com.hospital.management.domain.entity.PrescriptionMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrescriptionMedicineRepository extends JpaRepository<PrescriptionMedicine, UUID> {

    List<PrescriptionMedicine> findByPrescriptionId(UUID prescriptionId);
}
