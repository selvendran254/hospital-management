package com.hospital.management.repository;

import com.hospital.management.domain.entity.TelemedicineSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TelemedicineSessionRepository extends JpaRepository<TelemedicineSession, UUID> {
}
