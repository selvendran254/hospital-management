package com.hospital.management.repository;

import com.hospital.management.domain.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByUserId(UUID userId);
    long countByClinicId(UUID clinicId);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR p.phone LIKE CONCAT('%', :query, '%')")
    Page<Patient> search(@Param("query") String query, Pageable pageable);
}
