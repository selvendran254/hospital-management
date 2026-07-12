package com.hospital.management.repository;

import com.hospital.management.domain.entity.MediaAsset;
import com.hospital.management.domain.enums.MediaCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, UUID> {
    List<MediaAsset> findByClinicIdAndCategory(UUID clinicId, MediaCategory category);
}
