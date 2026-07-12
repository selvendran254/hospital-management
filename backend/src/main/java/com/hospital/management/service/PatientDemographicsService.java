package com.hospital.management.service;

import com.hospital.management.domain.entity.Patient;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientDemographicsService {

    private final PatientRepository patientRepository;
    private final ClinicContextService clinicContextService;

    public FeatureResponses.PatientDemographicsResponse analyze() {
        UUID clinicId = clinicContextService.resolveClinicId();
        List<Patient> patients = patientRepository.findAll().stream()
                .filter(p -> clinicId.equals(p.getClinicId()))
                .toList();

        Map<String, Long> gender = new LinkedHashMap<>();
        Map<String, Long> ageBands = new LinkedHashMap<>();
        Map<String, Long> locations = new LinkedHashMap<>();

        for (Patient patient : patients) {
            String genderKey = patient.getGender() == null ? "UNKNOWN" : patient.getGender().name();
            gender.merge(genderKey, 1L, Long::sum);

            if (patient.getDateOfBirth() != null) {
                int age = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();
                String band = age < 18 ? "0-17" : age < 36 ? "18-35" : age < 56 ? "36-55" : "56+";
                ageBands.merge(band, 1L, Long::sum);
            } else {
                ageBands.merge("UNKNOWN", 1L, Long::sum);
            }

            String location = patient.getAddress() == null || patient.getAddress().isBlank() ? "UNKNOWN"
                    : patient.getAddress().split(",")[0].trim().toUpperCase();
            locations.merge(location, 1L, Long::sum);
        }

        return FeatureResponses.PatientDemographicsResponse.builder()
                .genderDistribution(gender)
                .ageBands(ageBands)
                .locationDistribution(locations)
                .build();
    }
}
