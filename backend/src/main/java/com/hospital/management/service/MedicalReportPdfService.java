package com.hospital.management.service;

import com.hospital.management.domain.entity.Patient;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalReportPdfService {

    private final PatientRepository patientRepository;

    public byte[] exportPatientSummaryPdf(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + patientId));
        String content = "MEDICAL SUMMARY\nPatient: " + patient.getFirstName() + " " + patient.getLastName()
                + "\nBlood Group: " + patient.getBloodGroup()
                + "\nMedical History: " + patient.getMedicalHistory();
        return content.getBytes(StandardCharsets.UTF_8);
    }
}
