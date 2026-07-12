package com.hospital.management.service;

import com.hospital.management.domain.entity.InsuranceClaim;
import com.hospital.management.domain.enums.InsuranceClaimStatus;
import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.InsuranceClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsuranceClaimService {

    private final InsuranceClaimRepository insuranceClaimRepository;
    private final ClinicContextService clinicContextService;

    @Transactional
    public InsuranceClaim submit(FeatureRequests.InsuranceClaimRequest request) {
        return insuranceClaimRepository.save(InsuranceClaim.builder()
                .clinicId(clinicContextService.resolveClinicId())
                .patientId(request.getPatientId())
                .billId(request.getBillId())
                .insuranceProvider(request.getInsuranceProvider())
                .policyNumber(request.getPolicyNumber())
                .claimAmount(request.getClaimAmount())
                .status(InsuranceClaimStatus.SUBMITTED)
                .build());
    }

    @Transactional
    public InsuranceClaim updateStatus(UUID claimId, FeatureRequests.InsuranceClaimStatusRequest request) {
        InsuranceClaim claim = insuranceClaimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found: " + claimId));
        claim.setStatus(request.getStatus());
        claim.setReviewNotes(request.getReviewNotes());
        claim.setProcessedAt(Instant.now());
        return insuranceClaimRepository.save(claim);
    }

    public List<InsuranceClaim> list() {
        return insuranceClaimRepository.findByClinicId(clinicContextService.resolveClinicId());
    }
}
