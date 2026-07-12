package com.hospital.management.controller;

import com.hospital.management.domain.entity.DrugInteraction;
import com.hospital.management.domain.entity.MedicineOrder;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.service.DrugInteractionCheckerService;
import com.hospital.management.service.ExtensionCrudService;
import com.hospital.management.service.LabReportEmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pharmacy-lab/ext")
@RequiredArgsConstructor
public class PharmacyLabExtensionController {

    private final ExtensionCrudService extensionCrudService;
    private final DrugInteractionCheckerService drugInteractionCheckerService;
    private final LabReportEmailService labReportEmailService;

    @PostMapping("/drug-interactions")
    public ResponseEntity<DrugInteraction> createInteraction(@Valid @RequestBody ExtensionRequests.DrugInteractionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createDrugInteraction(request));
    }

    @GetMapping("/drug-interactions/check")
    public ResponseEntity<ExtensionResponses.DrugInteractionCheckResponse> check(
            @RequestParam String drugA,
            @RequestParam String drugB) {
        return ResponseEntity.ok(drugInteractionCheckerService.check(drugA, drugB));
    }

    @PostMapping("/medicine-orders")
    public ResponseEntity<MedicineOrder> createOrder(@Valid @RequestBody ExtensionRequests.MedicineOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createMedicineOrder(request));
    }

    @PostMapping("/lab-report-email")
    public ResponseEntity<Void> sendLabReportEmail(@RequestParam String to, @RequestParam String patientName) {
        labReportEmailService.sendLabReportReady(to, patientName);
        return ResponseEntity.accepted().build();
    }
}
