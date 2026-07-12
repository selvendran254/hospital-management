package com.hospital.management.service;

import com.hospital.management.domain.entity.Patient;
import com.hospital.management.domain.entity.User;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.request.PatientRequest;
import com.hospital.management.dto.request.RegisterPatientRequest;
import com.hospital.management.dto.response.MedicalRecordResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.PatientResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.DoctorRepository;
import com.hospital.management.repository.MedicalRecordRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityMapper entityMapper;
    private final ClinicContextService clinicContextService;

    public PageResponse<PatientResponse> getAll(int page, int size, String query, String sortBy, String sortDir) {
        Pageable pageable = PageUtils.of(page, size, sortBy, sortDir);
        if (query != null && !query.isBlank()) {
            return PageResponse.from(patientRepository.search(query, pageable).map(entityMapper::toPatientResponse));
        }
        return PageResponse.from(patientRepository.findAll(pageable).map(entityMapper::toPatientResponse));
    }

    public PatientResponse getById(UUID id) {
        return entityMapper.toPatientResponse(findPatient(id));
    }

    @Transactional
    public PatientResponse create(PatientRequest request) {
        return entityMapper.toPatientResponse(createPatientInternal(request));
    }

    @Transactional
    public PatientResponse registerPatient(RegisterPatientRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        PatientRequest patientRequest = new PatientRequest();
        patientRequest.setEmail(request.getEmail());
        patientRequest.setPassword(request.getPassword());
        patientRequest.setFirstName(request.getFirstName());
        patientRequest.setLastName(request.getLastName());
        patientRequest.setPhone(request.getPhone());
        patientRequest.setDateOfBirth(request.getDateOfBirth());
        patientRequest.setGender(request.getGender());
        patientRequest.setBloodGroup(request.getBloodGroup());
        patientRequest.setAddress(request.getAddress());
        patientRequest.setEmergencyContact(request.getEmergencyContact());
        return entityMapper.toPatientResponse(createPatientInternal(patientRequest));
    }

    @Transactional
    public PatientResponse update(UUID id, PatientRequest request) {
        Patient patient = findPatient(id);
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPhone(request.getPhone());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAddress(request.getAddress());
        patient.setEmergencyContact(request.getEmergencyContact());
        patient.setMedicalHistory(request.getMedicalHistory());
        return entityMapper.toPatientResponse(patientRepository.save(patient));
    }

    @Transactional
    public void delete(UUID id) {
        Patient patient = findPatient(id);
        userRepository.findById(patient.getUserId()).ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
        });
    }

    public List<MedicalRecordResponse> getMedicalHistory(UUID patientId) {
        findPatient(patientId);
        return medicalRecordRepository.findByPatientIdOrderByVisitDateDesc(patientId).stream()
                .map(record -> {
                    String doctorName = null;
                    if (record.getDoctorId() != null) {
                        doctorName = doctorRepository.findById(record.getDoctorId())
                                .map(d -> d.getFirstName() + " " + d.getLastName())
                                .orElse(null);
                    }
                    return MedicalRecordResponse.builder()
                            .id(record.getId())
                            .patientId(record.getPatientId())
                            .doctorId(record.getDoctorId())
                            .doctorName(doctorName)
                            .visitDate(record.getVisitDate())
                            .chiefComplaint(record.getChiefComplaint())
                            .diagnosis(record.getDiagnosis())
                            .treatment(record.getTreatment())
                            .vitals(record.getVitals())
                            .createdAt(record.getCreatedAt())
                            .build();
                })
                .toList();
    }

    private Patient createPatientInternal(PatientRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered");
        }
        User user = User.builder()
                .clinicId(resolveClinicId())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.PATIENT)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .clinicId(resolveClinicId())
                .userId(user.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .address(request.getAddress())
                .emergencyContact(request.getEmergencyContact())
                .medicalHistory(request.getMedicalHistory())
                .build();
        return patientRepository.save(patient);
    }

    private Patient findPatient(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + id));
    }

    private UUID resolveClinicId() {
        return clinicContextService != null ? clinicContextService.resolveClinicId() : null;
    }
}
