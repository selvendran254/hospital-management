package com.hospital.management.service;

import com.hospital.management.dto.response.CareerResponse;
import com.hospital.management.dto.response.HealthPackageResponse;
import com.hospital.management.dto.response.HospitalServiceResponse;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.CareerRepository;
import com.hospital.management.repository.HealthPackageRepository;
import com.hospital.management.repository.HospitalServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicService {

    private final HospitalServiceRepository hospitalServiceRepository;
    private final HealthPackageRepository healthPackageRepository;
    private final CareerRepository careerRepository;
    private final EntityMapper entityMapper;

    public List<HospitalServiceResponse> getServices() {
        return hospitalServiceRepository.findByActiveTrue().stream()
                .map(entityMapper::toHospitalServiceResponse)
                .toList();
    }

    public List<HealthPackageResponse> getHealthPackages() {
        return healthPackageRepository.findByActiveTrue().stream()
                .map(entityMapper::toHealthPackageResponse)
                .toList();
    }

    public List<CareerResponse> getCareers() {
        return careerRepository.findByActiveTrue().stream()
                .map(entityMapper::toCareerResponse)
                .toList();
    }
}
