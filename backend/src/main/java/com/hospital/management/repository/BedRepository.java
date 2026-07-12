package com.hospital.management.repository;

import com.hospital.management.domain.entity.Bed;
import com.hospital.management.domain.enums.BedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BedRepository extends JpaRepository<Bed, UUID> {

    List<Bed> findByRoomId(UUID roomId);

    List<Bed> findByStatus(BedStatus status);

    List<Bed> findByRoomIdAndStatus(UUID roomId, BedStatus status);
}
