package com.hospital.management.repository;

import com.hospital.management.domain.entity.HealthPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HealthPackageRepository extends JpaRepository<HealthPackage, UUID> {

    List<HealthPackage> findByActiveTrue();
}
