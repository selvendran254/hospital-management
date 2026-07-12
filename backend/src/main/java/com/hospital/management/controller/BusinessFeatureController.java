package com.hospital.management.controller;

import com.hospital.management.domain.entity.InsuranceClaim;
import com.hospital.management.domain.entity.PatientFeedback;
import com.hospital.management.domain.entity.StaffAttendance;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.service.BusinessFeatureService;
import com.hospital.management.service.InsuranceClaimService;
import com.hospital.management.service.InvoicePdfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessFeatureController {

    private final BusinessFeatureService businessFeatureService;
    private final InsuranceClaimService insuranceClaimService;
    private final InvoicePdfService invoicePdfService;

    @PostMapping("/attendance")
    public ResponseEntity<StaffAttendance> markAttendance(@Valid @RequestBody FeatureRequests.AttendanceRequest request) {
        return ResponseEntity.ok(businessFeatureService.markAttendance(request));
    }

    @GetMapping("/attendance/{staffId}")
    public ResponseEntity<List<StaffAttendance>> attendance(@PathVariable UUID staffId) {
        return ResponseEntity.ok(businessFeatureService.staffAttendance(staffId));
    }

    @PostMapping("/insurance-claims")
    public ResponseEntity<InsuranceClaim> createClaim(@Valid @RequestBody FeatureRequests.InsuranceClaimRequest request) {
        return ResponseEntity.ok(insuranceClaimService.submit(request));
    }

    @PatchMapping("/insurance-claims/{claimId}")
    public ResponseEntity<InsuranceClaim> updateClaim(@PathVariable UUID claimId,
                                                      @Valid @RequestBody FeatureRequests.InsuranceClaimStatusRequest request) {
        return ResponseEntity.ok(insuranceClaimService.updateStatus(claimId, request));
    }

    @GetMapping("/insurance-claims")
    public ResponseEntity<List<InsuranceClaim>> claims() {
        return ResponseEntity.ok(insuranceClaimService.list());
    }

    @PostMapping("/feedback")
    public ResponseEntity<PatientFeedback> addFeedback(@Valid @RequestBody FeatureRequests.PatientFeedbackRequest request) {
        return ResponseEntity.ok(businessFeatureService.saveFeedback(request));
    }

    @GetMapping("/feedback/doctor/{doctorId}")
    public ResponseEntity<List<PatientFeedback>> doctorFeedback(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(businessFeatureService.feedbackByDoctor(doctorId));
    }

    @GetMapping("/invoice/{billId}/pdf")
    public ResponseEntity<byte[]> invoicePdf(@PathVariable UUID billId) {
        byte[] pdf = invoicePdfService.generateInvoice(billId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("invoice-" + billId + ".pdf").build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
