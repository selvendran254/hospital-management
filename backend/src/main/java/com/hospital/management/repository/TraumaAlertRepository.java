package com.hospital.management.repository;

import com.hospital.management.domain.entity.TraumaAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TraumaAlertRepository extends JpaRepository<TraumaAlert, UUID> {
}
