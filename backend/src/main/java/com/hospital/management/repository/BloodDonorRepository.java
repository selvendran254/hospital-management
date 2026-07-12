package com.hospital.management.repository;

import com.hospital.management.domain.entity.BloodDonor;
import com.hospital.management.domain.enums.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BloodDonorRepository extends JpaRepository<BloodDonor, UUID> {

    List<BloodDonor> findByBloodGroup(BloodGroup bloodGroup);
}
