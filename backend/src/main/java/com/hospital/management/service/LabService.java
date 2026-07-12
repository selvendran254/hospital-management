package com.hospital.management.service;

import com.hospital.management.domain.entity.LabReport;
import com.hospital.management.domain.entity.LabTest;
import com.hospital.management.domain.entity.Patient;
import com.hospital.management.domain.entity.User;
import com.hospital.management.domain.enums.NotificationType;
import com.hospital.management.domain.enums.UserRole;
import com.hospital.management.dto.request.LabReportRequest;
import com.hospital.management.dto.request.LabTestRequest;
import com.hospital.management.dto.response.LabReportResponse;
import com.hospital.management.dto.response.LabTestResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.LabReportRepository;
import com.hospital.management.repository.LabTestRepository;
import com.hospital.management.repository.PatientRepository;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LabService {

    private final LabTestRepository labTestRepository;
    private final LabReportRepository labReportRepository;
    private final PatientRepository patientRepository;
    private final NotificationService notificationService;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;

    public PageResponse<LabTestResponse> getTests(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "name", "asc");
        return PageResponse.from(labTestRepository.findAll(pageable).map(entityMapper::toLabTestResponse));
    }

    public LabTestResponse getTestById(UUID id) {
        return entityMapper.toLabTestResponse(findTest(id));
    }

    @Transactional
    public LabTestResponse createTest(LabTestRequest request) {
        LabTest test = LabTest.builder()
                .name(request.getName())
                .code(request.getCode())
                .barcode(request.getBarcode())
                .description(request.getDescription())
                .price(request.getPrice())
                .departmentId(request.getDepartmentId())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return entityMapper.toLabTestResponse(labTestRepository.save(test));
    }

    @Transactional
    public LabTestResponse updateTest(UUID id, LabTestRequest request) {
        LabTest test = findTest(id);
        test.setName(request.getName());
        test.setCode(request.getCode());
        test.setBarcode(request.getBarcode());
        test.setDescription(request.getDescription());
        test.setPrice(request.getPrice());
        test.setDepartmentId(request.getDepartmentId());
        if (request.getActive() != null) test.setActive(request.getActive());
        return entityMapper.toLabTestResponse(labTestRepository.save(test));
    }

    @Transactional
    public void deleteTest(UUID id) {
        LabTest test = findTest(id);
        test.setActive(false);
        labTestRepository.save(test);
    }

    public PageResponse<LabReportResponse> getReports(int page, int size, String status) {
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        if (status != null && !status.isBlank()) {
            boolean completed = "COMPLETED".equalsIgnoreCase(status);
            return PageResponse.from(labReportRepository.findByCompleted(completed, pageable)
                    .map(entityMapper::toLabReportResponse));
        }
        return PageResponse.from(labReportRepository.findAll(pageable).map(entityMapper::toLabReportResponse));
    }

    public LabReportResponse getReportById(UUID id) {
        return entityMapper.toLabReportResponse(findReport(id));
    }

    @Transactional
    public LabReportResponse updateReport(UUID id, LabReportRequest request) {
        LabReport report = findReport(id);
        if (request.getResultSummary() != null) report.setResultSummary(request.getResultSummary());
        if (request.getReportFileUrl() != null) report.setReportFileUrl(request.getReportFileUrl());
        if (request.getCompleted() != null) {
            report.setCompleted(request.getCompleted());
            if (Boolean.TRUE.equals(request.getCompleted())) {
                report.setCompletedAt(Instant.now());
                patientRepository.findById(report.getPatientId()).ifPresent(patient ->
                        notificationService.notifyUser(patient.getUserId(), "Lab Report Ready",
                                "Your lab report is now available.", NotificationType.LAB_REPORT_READY));
            }
        }
        return entityMapper.toLabReportResponse(labReportRepository.save(report));
    }

    public PageResponse<LabReportResponse> getMyReports(int page, int size) {
        User user = securityUtils.getCurrentUser();
        if (user.getRole() != UserRole.PATIENT) {
            return getReports(page, size, null);
        }
        Patient patient = patientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        Pageable pageable = PageUtils.of(page, size, "createdAt", "desc");
        return PageResponse.from(labReportRepository.findByPatientId(patient.getId(), pageable)
                .map(entityMapper::toLabReportResponse));
    }

    private LabTest findTest(UUID id) {
        return labTestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab test not found: " + id));
    }

    private LabReport findReport(UUID id) {
        return labReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab report not found: " + id));
    }
}
