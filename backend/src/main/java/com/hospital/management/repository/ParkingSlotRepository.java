package com.hospital.management.repository;

import com.hospital.management.domain.entity.ParkingSlot;
import com.hospital.management.domain.enums.ParkingSlotStatus;
import com.hospital.management.domain.enums.ParkingSlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, UUID> {
    List<ParkingSlot> findByStatus(ParkingSlotStatus status);
    List<ParkingSlot> findBySlotType(ParkingSlotType slotType);
    long countByStatus(ParkingSlotStatus status);
}
