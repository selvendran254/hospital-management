package com.hospital.management.repository;

import com.hospital.management.domain.entity.Ambulance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, UUID> {

    Optional<Ambulance> findByVehicleNumber(String vehicleNumber);

    List<Ambulance> findByAvailableTrue();
}
