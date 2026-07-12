package com.hospital.management.repository;

import com.hospital.management.domain.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FaqRepository extends JpaRepository<Faq, UUID> {
}
