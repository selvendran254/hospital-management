package com.hospital.management.repository;

import com.hospital.management.domain.entity.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface QueueTokenRepository extends JpaRepository<QueueToken, UUID> {
    List<QueueToken> findByDoctorIdAndQueueDateOrderByTokenNumber(UUID doctorId, LocalDate queueDate);
}
