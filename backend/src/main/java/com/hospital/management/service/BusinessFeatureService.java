package com.hospital.management.service;

import com.hospital.management.domain.entity.PatientFeedback;
import com.hospital.management.domain.entity.StaffAttendance;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.repository.PatientFeedbackRepository;
import com.hospital.management.repository.StaffAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BusinessFeatureService {

    private final StaffAttendanceRepository staffAttendanceRepository;
    private final PatientFeedbackRepository patientFeedbackRepository;
    private final ClinicContextService clinicContextService;

    @Transactional
    public StaffAttendance markAttendance(FeatureRequests.AttendanceRequest request) {
        return staffAttendanceRepository.save(StaffAttendance.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .staffId(request.getStaffId())
                .biometricId(request.getBiometricId())
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .method(request.getMethod())
                .build());
    }

    public List<StaffAttendance> staffAttendance(UUID staffId) {
        return staffAttendanceRepository.findByClinicIdAndStaffId(clinicContextService.resolveClinicId(), staffId);
    }

    @Transactional
    public PatientFeedback saveFeedback(FeatureRequests.PatientFeedbackRequest request) {
        return patientFeedbackRepository.save(PatientFeedback.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .rating(request.getRating())
                .review(request.getReview())
                .build());
    }

    public List<PatientFeedback> feedbackByDoctor(UUID doctorId) {
        return patientFeedbackRepository.findByClinicIdAndDoctorId(clinicContextService.resolveClinicId(), doctorId);
    }
}
