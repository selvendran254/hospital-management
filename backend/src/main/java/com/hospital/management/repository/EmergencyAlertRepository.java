package com.hospital.management.repository;

import com.hospital.management.domain.entity.EmergencyAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmergencyAlertRepository extends JpaRepository<EmergencyAlert, UUID> {
}
