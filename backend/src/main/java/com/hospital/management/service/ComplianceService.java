package com.hospital.management.service;

import com.hospital.management.domain.entity.AuditLog;
import com.hospital.management.domain.entity.ConsentRecord;
import com.hospital.management.domain.entity.PatientAccessLog;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.repository.AuditLogRepository;
import com.hospital.management.repository.ConsentRecordRepository;
import com.hospital.management.repository.PatientAccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComplianceService {

    private final AuditLogRepository auditLogRepository;
    private final ConsentRecordRepository consentRecordRepository;
    private final PatientAccessLogRepository patientAccessLogRepository;
    private final ClinicContextService clinicContextService;

    @Value("${app.compliance.encryption-at-rest:true}")
    private boolean encryptionAtRest;
    @Value("${app.compliance.encryption-in-transit:true}")
    private boolean encryptionInTransit;

    @Transactional
    public ConsentRecord saveConsent(FeatureRequests.ConsentRecordRequest request) {
        ConsentRecord record = consentRecordRepository.save(ConsentRecord.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .patientId(request.getPatientId())
                .consentType(request.getConsentType())
                .granted(request.getGranted())
                .metadataJson(request.getMetadataJson())
                .build());
        auditLogRepository.save(AuditLog.builder()
                .action("CONSENT_" + (request.getGranted() ? "GRANTED" : "REVOKED"))
                .entityName("ConsentRecord")
                .entityId(record.getId().toString())
                .performedBy("system")
                .details("Consent type: " + request.getConsentType())
                .build());
        return record;
    }

    @Transactional
    public PatientAccessLog logPatientAccess(FeatureRequests.PatientAccessLogRequest request) {
        PatientAccessLog log = patientAccessLogRepository.save(PatientAccessLog.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .patientId(request.getPatientId())
                .viewerUserId(request.getViewerUserId())
                .viewerRole(request.getViewerRole())
                .action(request.getAction())
                .accessedAt(Instant.now())
                .build());
        auditLogRepository.save(AuditLog.builder()
                .action("PATIENT_RECORD_VIEWED")
                .entityName("Patient")
                .entityId(request.getPatientId().toString())
                .performedBy(request.getViewerUserId().toString())
                .details("Access log id: " + log.getId())
                .build());
        return log;
    }

    public List<AuditLog> auditTrail() {
        return auditLogRepository.findTop100ByOrderByCreatedAtDesc();
    }

    public FeatureResponses.ComplianceMetadataResponse ndhmMetadata() {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("consentManagerEnabled", true);
        metadata.put("healthIdMapping", true);
        metadata.put("auditRetentionDays", 3650);
        metadata.put("encryptionAtRest", encryptionAtRest);
        metadata.put("encryptionInTransit", encryptionInTransit);
        return FeatureResponses.ComplianceMetadataResponse.builder()
                .framework("NDHM")
                .metadata(metadata)
                .build();
    }

    public FeatureResponses.ComplianceMetadataResponse hipaaMetadata() {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("phiMasking", true);
        metadata.put("breakGlassAccess", true);
        metadata.put("auditTrailEnabled", true);
        metadata.put("encryptionAtRest", encryptionAtRest);
        metadata.put("encryptionInTransit", encryptionInTransit);
        return FeatureResponses.ComplianceMetadataResponse.builder()
                .framework("HIPAA")
                .metadata(metadata)
                .build();
    }
}
