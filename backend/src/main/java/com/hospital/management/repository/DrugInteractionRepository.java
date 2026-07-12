package com.hospital.management.repository;

import com.hospital.management.domain.entity.DrugInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DrugInteractionRepository extends JpaRepository<DrugInteraction, UUID> {
    List<DrugInteraction> findByDrugAIgnoreCaseOrDrugBIgnoreCase(String drugA, String drugB);
}
