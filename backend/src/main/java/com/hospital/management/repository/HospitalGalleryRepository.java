package com.hospital.management.repository;

import com.hospital.management.domain.entity.HospitalGallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HospitalGalleryRepository extends JpaRepository<HospitalGallery, UUID> {
}
