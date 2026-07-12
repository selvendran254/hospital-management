package com.hospital.management.service;

import com.hospital.management.domain.entity.*;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.request.PrescriptionMedicineRequest;
import com.hospital.management.dto.request.PrescriptionRequest;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.PrescriptionResponse;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.*;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMedicineRepository prescriptionMedicineRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;

    public PageResponse<PrescriptionResponse> getAll(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        return PageResponse.from(prescriptionRepository.findAll(pageable).map(entityMapper::toPrescriptionResponse));
    }

    public PrescriptionResponse getById(UUID id) {
        return entityMapper.toPrescriptionResponse(findPrescription(id));
    }

    @Transactional
    public PrescriptionResponse create(PrescriptionRequest request) {
        patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Prescription prescription = Prescription.builder()
                .appointmentId(request.getAppointmentId())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .diagnosis(request.getDiagnosis())
                .instructions(request.getInstructions())
                .build();
        prescription = prescriptionRepository.save(prescription);

        if (request.getMedicines() != null) {
            List<PrescriptionMedicine> medicines = new ArrayList<>();
            for (PrescriptionMedicineRequest medRequest : request.getMedicines()) {
                PrescriptionMedicine medicine = PrescriptionMedicine.builder()
                        .prescriptionId(prescription.getId())
                        .medicineName(medRequest.getMedicineName())
                        .dosage(medRequest.getDosage())
                        .frequency(medRequest.getFrequency())
                        .duration(medRequest.getDuration())
                        .build();
                medicines.add(prescriptionMedicineRepository.save(medicine));
            }
            prescription.setMedicines(medicines);
        }
        return entityMapper.toPrescriptionResponse(prescription);
    }

    public PageResponse<PrescriptionResponse> getMyPrescriptions(int page, int size) {
        User user = securityUtils.getCurrentUser();
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        if (user.getRole() == UserRole.PATIENT) {
            Patient patient = patientRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
            return PageResponse.from(prescriptionRepository.findByPatientId(patient.getId(), pageable)
                    .map(entityMapper::toPrescriptionResponse));
        }
        if (user.getRole() == UserRole.DOCTOR) {
            Doctor doctor = doctorRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));
            return PageResponse.from(prescriptionRepository.findByDoctorId(doctor.getId(), pageable)
                    .map(entityMapper::toPrescriptionResponse));
        }
        return getAll(page, size);
    }

    private Prescription findPrescription(UUID id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription not found: " + id));
    }
}
