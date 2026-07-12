package com.hospital.management.repository;

import com.hospital.management.domain.entity.MedicineOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicineOrderRepository extends JpaRepository<MedicineOrder, UUID> {
}
