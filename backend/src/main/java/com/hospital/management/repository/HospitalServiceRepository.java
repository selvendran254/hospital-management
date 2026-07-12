package com.hospital.management.repository;

import com.hospital.management.domain.entity.HospitalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HospitalServiceRepository extends JpaRepository<HospitalService, UUID> {

    List<HospitalService> findByActiveTrue();
}
