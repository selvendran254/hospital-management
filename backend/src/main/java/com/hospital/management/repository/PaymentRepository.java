package com.hospital.management.repository;

import com.hospital.management.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByBillId(UUID billId);
    List<Payment> findByClinicId(UUID clinicId);
}
