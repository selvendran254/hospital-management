package com.hospital.management.repository;

import com.hospital.management.domain.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    java.util.List<AuditLog> findTop100ByOrderByCreatedAtDesc();
}
