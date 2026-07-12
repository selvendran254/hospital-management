package com.hospital.management.repository;

import com.hospital.management.domain.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReferralRepository extends JpaRepository<Referral, UUID> {
}
