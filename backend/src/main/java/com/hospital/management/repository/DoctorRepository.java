package com.hospital.management.repository;

import com.hospital.management.domain.entity.Doctor;
import com.hospital.management.domain.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    Optional<Doctor> findByUserId(UUID userId);
    List<Doctor> findByClinicId(UUID clinicId);

    List<Doctor> findByDepartmentId(UUID departmentId);

    List<Doctor> findByAvailableTrue();

    Page<Doctor> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName, Pageable pageable);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Doctor> search(@Param("query") String query, Pageable pageable);

    @Query("""
            SELECT d FROM Doctor d
            WHERE (:query IS NULL OR :query = '' OR
                   LOWER(d.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                   LOWER(d.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR
                   LOWER(d.specialization) LIKE LOWER(CONCAT('%', :query, '%')))
              AND (:departmentId IS NULL OR d.departmentId = :departmentId)
              AND (:gender IS NULL OR d.gender = :gender)
              AND (:available IS NULL OR d.available = :available)
              AND (:minExperience IS NULL OR d.experienceYears >= :minExperience)
              AND (:maxExperience IS NULL OR d.experienceYears <= :maxExperience)
            """)
    Page<Doctor> searchWithFilters(
            @Param("query") String query,
            @Param("departmentId") UUID departmentId,
            @Param("gender") Gender gender,
            @Param("available") Boolean available,
            @Param("minExperience") Integer minExperience,
            @Param("maxExperience") Integer maxExperience,
            Pageable pageable);
}
