package com.hospital.management.controller;

import com.hospital.management.domain.entity.AuditLog;
import com.hospital.management.domain.entity.ConsentRecord;
import com.hospital.management.domain.entity.PatientAccessLog;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.dto.response.MessageResponse;
import com.hospital.management.service.ComplianceService;
import com.hospital.management.service.DatabaseBackupService;
import com.hospital.management.service.TwoFactorAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
public class SecurityFeatureController {

    private final TwoFactorAuthService twoFactorAuthService;
    private final DatabaseBackupService databaseBackupService;
    private final ComplianceService complianceService;

    @PostMapping("/2fa/setup")
    public ResponseEntity<Map<String, String>> setup2fa() {
        return ResponseEntity.ok(twoFactorAuthService.setupCurrentUser());
    }

    @PostMapping("/2fa/verify")
    public ResponseEntity<MessageResponse> verify2fa(@Valid @RequestBody FeatureRequests.TwoFactorVerifyRequest request) {
        boolean verified = twoFactorAuthService.verifyCurrentUser(request);
        return ResponseEntity.ok(MessageResponse.of(verified ? "2FA verified" : "Invalid OTP"));
    }

    @PostMapping("/backup/trigger")
    public ResponseEntity<Map<String, String>> backupNow() throws IOException, InterruptedException {
        String path = databaseBackupService.triggerBackup();
        return ResponseEntity.ok(Map.of("backupPath", path));
    }

    @GetMapping("/compliance/metadata/ndhm")
    public ResponseEntity<FeatureResponses.ComplianceMetadataResponse> ndhm() {
        return ResponseEntity.ok(complianceService.ndhmMetadata());
    }

    @GetMapping("/compliance/metadata/hipaa")
    public ResponseEntity<FeatureResponses.ComplianceMetadataResponse> hipaa() {
        return ResponseEntity.ok(complianceService.hipaaMetadata());
    }

    @PostMapping("/compliance/consent")
    public ResponseEntity<ConsentRecord> consent(@Valid @RequestBody FeatureRequests.ConsentRecordRequest request) {
        return ResponseEntity.ok(complianceService.saveConsent(request));
    }

    @PostMapping("/compliance/patient-access")
    public ResponseEntity<PatientAccessLog> accessLog(@Valid @RequestBody FeatureRequests.PatientAccessLogRequest request) {
        return ResponseEntity.ok(complianceService.logPatientAccess(request));
    }

    @GetMapping("/compliance/audit-trail")
    public ResponseEntity<List<AuditLog>> auditTrail() {
        return ResponseEntity.ok(complianceService.auditTrail());
    }
}
