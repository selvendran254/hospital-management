package com.hospital.management.repository;

import com.hospital.management.domain.entity.Medicine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {

    List<Medicine> findByActiveTrue();

    Page<Medicine> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT m FROM Medicine m WHERE m.active = true AND m.stockQuantity <= m.reorderLevel")
    List<Medicine> findLowStockMedicines();
}
