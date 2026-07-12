package com.hospital.management.repository;

import com.hospital.management.domain.entity.TenantBranding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantBrandingRepository extends JpaRepository<TenantBranding, UUID> {
}
