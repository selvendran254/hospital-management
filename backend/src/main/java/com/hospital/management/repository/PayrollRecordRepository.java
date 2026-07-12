package com.hospital.management.repository;

import com.hospital.management.domain.entity.PayrollRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PayrollRecordRepository extends JpaRepository<PayrollRecord, UUID> {
}
