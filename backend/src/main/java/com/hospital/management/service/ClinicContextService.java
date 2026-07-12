package com.hospital.management.service;

import com.hospital.management.domain.entity.Clinic;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClinicContextService {

    private static final String CLINIC_HEADER = "X-Clinic-Id";
    private final ClinicRepository clinicRepository;

    public UUID resolveClinicId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String value = attributes.getRequest().getHeader(CLINIC_HEADER);
            if (value != null && !value.isBlank()) {
                try {
                    UUID clinicId = UUID.fromString(value);
                    if (clinicRepository.existsById(clinicId)) {
                        return clinicId;
                    }
                    throw new BadRequestException("Unknown clinic id in header");
                } catch (IllegalArgumentException ex) {
                    throw new BadRequestException("Invalid X-Clinic-Id header");
                }
            }
        }
        return clinicRepository.findAll().stream()
                .findFirst()
                .map(Clinic::getId)
                .orElseThrow(() -> new BadRequestException("No clinic configured"));
    }
}
