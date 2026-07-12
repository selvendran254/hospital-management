package com.hospital.management.controller;

import com.hospital.management.domain.entity.HealthTrackerRecord;
import com.hospital.management.domain.entity.InsurancePolicy;
import com.hospital.management.domain.entity.MedicineReminder;
import com.hospital.management.domain.entity.VaccinationRecord;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.service.ExtensionCrudService;
import com.hospital.management.service.MedicalReportPdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients/ext")
@RequiredArgsConstructor
public class PatientExtensionController {

    private final ExtensionCrudService extensionCrudService;
    private final MedicalReportPdfService medicalReportPdfService;

    @PostMapping("/health-tracker")
    public ResponseEntity<HealthTrackerRecord> createTracker(@Valid @RequestBody ExtensionRequests.HealthTrackerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createHealthTracker(request));
    }

    @GetMapping("/health-tracker/{patientId}")
    public ResponseEntity<List<HealthTrackerRecord>> getTracker(@PathVariable UUID patientId) {
        return ResponseEntity.ok(extensionCrudService.getHealthTracker(patientId));
    }

    @PostMapping("/medicine-reminders")
    public ResponseEntity<MedicineReminder> createReminder(@Valid @RequestBody ExtensionRequests.MedicineReminderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createMedicineReminder(request));
    }

    @PostMapping("/insurance-policies")
    public ResponseEntity<InsurancePolicy> createPolicy(@Valid @RequestBody ExtensionRequests.InsurancePolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createInsurancePolicy(request));
    }

    @PostMapping("/vaccinations")
    public ResponseEntity<VaccinationRecord> createVaccination(@Valid @RequestBody ExtensionRequests.VaccinationRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createVaccinationRecord(request));
    }

    @GetMapping("/{patientId}/medical-report.pdf")
    public ResponseEntity<byte[]> exportPdf(@PathVariable UUID patientId) {
        byte[] content = medicalReportPdfService.exportPatientSummaryPdf(patientId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medical-report.pdf")
                .body(content);
    }
}
