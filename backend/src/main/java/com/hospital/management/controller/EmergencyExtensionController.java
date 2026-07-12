package com.hospital.management.controller;

import com.hospital.management.domain.entity.AmbulanceTracking;
import com.hospital.management.domain.entity.EmergencyAlert;
import com.hospital.management.domain.entity.TraumaAlert;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.service.ExtensionCrudService;
import com.hospital.management.service.IcuMonitorService;
import com.hospital.management.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emergency/ext")
@RequiredArgsConstructor
public class EmergencyExtensionController {

    private final ExtensionCrudService extensionCrudService;
    private final IcuMonitorService icuMonitorService;
    private final NotificationService notificationService;

    @PostMapping("/ambulance-tracking")
    public ResponseEntity<AmbulanceTracking> createTracking(@Valid @RequestBody ExtensionRequests.AmbulanceTrackingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createAmbulanceTracking(request));
    }

    @PostMapping("/alerts")
    public ResponseEntity<EmergencyAlert> createEmergencyAlert(@Valid @RequestBody ExtensionRequests.EmergencyAlertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createEmergencyAlert(request));
    }

    @PostMapping("/trauma-alerts")
    public ResponseEntity<TraumaAlert> createTraumaAlert(@Valid @RequestBody ExtensionRequests.TraumaAlertRequest request) {
        TraumaAlert alert = extensionCrudService.createTraumaAlert(request);
        notificationService.notifyUser(java.util.UUID.randomUUID(), "Trauma Alert", "Critical trauma alert triggered",
                com.hospital.management.domain.enums.NotificationType.GENERAL);
        return ResponseEntity.status(HttpStatus.CREATED).body(alert);
    }

    @GetMapping("/icu-status")
    public ResponseEntity<ExtensionResponses.IcuStatusResponse> getIcuStatus() {
        return ResponseEntity.ok(icuMonitorService.getStatus());
    }
}
