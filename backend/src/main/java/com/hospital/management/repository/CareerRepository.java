package com.hospital.management.repository;

import com.hospital.management.domain.entity.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CareerRepository extends JpaRepository<Career, UUID> {

    List<Career> findByActiveTrue();
}
