package com.hospital.management.repository;

import com.hospital.management.domain.entity.BloodInventory;
import com.hospital.management.domain.enums.BloodGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BloodInventoryRepository extends JpaRepository<BloodInventory, UUID> {

    Optional<BloodInventory> findByBloodGroup(BloodGroup bloodGroup);
}
