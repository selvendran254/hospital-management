package com.hospital.management.service;

import com.hospital.management.domain.entity.Clinic;
import com.hospital.management.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepository clinicRepository;

    @Transactional
    public Clinic create(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    public List<Clinic> list() {
        return clinicRepository.findAll();
    }
}
