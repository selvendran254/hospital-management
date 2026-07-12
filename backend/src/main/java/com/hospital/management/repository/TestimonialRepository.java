package com.hospital.management.repository;

import com.hospital.management.domain.entity.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestimonialRepository extends JpaRepository<Testimonial, UUID> {
}
