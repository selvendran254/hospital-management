package com.hospital.management.repository;

import com.hospital.management.domain.entity.LabTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, UUID> {

    Optional<LabTest> findByCode(String code);

    List<LabTest> findByActiveTrue();

    Page<LabTest> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
