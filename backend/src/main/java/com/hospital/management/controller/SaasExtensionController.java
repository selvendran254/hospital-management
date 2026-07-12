package com.hospital.management.controller;

import com.hospital.management.domain.entity.OnboardingStep;
import com.hospital.management.domain.entity.SubscriptionPlan;
import com.hospital.management.domain.entity.TenantBranding;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.service.ExtensionCrudService;
import com.hospital.management.service.OwnerAnalyticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saas/ext")
@RequiredArgsConstructor
public class SaasExtensionController {

    private final ExtensionCrudService extensionCrudService;
    private final OwnerAnalyticsService ownerAnalyticsService;

    @PostMapping("/subscription-plans")
    public ResponseEntity<SubscriptionPlan> createPlan(@Valid @RequestBody ExtensionRequests.SubscriptionPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createSubscriptionPlan(request));
    }

    @PostMapping("/tenant-branding")
    public ResponseEntity<TenantBranding> createBranding(@Valid @RequestBody ExtensionRequests.TenantBrandingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createTenantBranding(request));
    }

    @PostMapping("/onboarding-steps")
    public ResponseEntity<OnboardingStep> createStep(@Valid @RequestBody ExtensionRequests.OnboardingStepRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createOnboardingStep(request));
    }

    @GetMapping("/owner-analytics")
    public ResponseEntity<ExtensionResponses.OwnerAnalyticsResponse> analytics() {
        return ResponseEntity.ok(ownerAnalyticsService.getExtendedStats());
    }
}
