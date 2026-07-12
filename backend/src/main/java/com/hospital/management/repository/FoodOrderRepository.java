package com.hospital.management.repository;

import com.hospital.management.domain.entity.FoodOrder;
import com.hospital.management.domain.enums.FoodOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, UUID> {
    List<FoodOrder> findByPatientIdOrderByCreatedAtDesc(UUID patientId);
    List<FoodOrder> findByStatusOrderByCreatedAtDesc(FoodOrderStatus status);
}
