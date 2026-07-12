package com.hospital.management.repository;

import com.hospital.management.domain.entity.TwoFactorAuthSecret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TwoFactorAuthSecretRepository extends JpaRepository<TwoFactorAuthSecret, UUID> {
    Optional<TwoFactorAuthSecret> findByUserId(UUID userId);
}
