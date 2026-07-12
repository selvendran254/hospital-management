package com.hospital.management.repository;

import com.hospital.management.domain.entity.HospitalBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HospitalBranchRepository extends JpaRepository<HospitalBranch, UUID> {
}
