package com.hospital.management.repository;

import com.hospital.management.domain.entity.CanteenMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CanteenMenuItemRepository extends JpaRepository<CanteenMenuItem, UUID> {
    List<CanteenMenuItem> findByAvailableTrue();
}
