package com.hospital.management.controller;

import com.hospital.management.dto.request.BloodDonorRequest;
import com.hospital.management.dto.request.BloodInventoryRequest;
import com.hospital.management.dto.request.BloodRequestRequest;
import com.hospital.management.dto.response.*;
import com.hospital.management.service.BloodBankService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blood")
@RequiredArgsConstructor
@Tag(name = "Blood Bank")
public class BloodBankController {

    private final BloodBankService bloodBankService;

    @GetMapping("/inventory")
    public ResponseEntity<List<BloodInventoryResponse>> getInventory() {
        return ResponseEntity.ok(bloodBankService.getInventory());
    }

    @PutMapping("/inventory/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<BloodInventoryResponse> updateInventory(
            @PathVariable UUID id,
            @Valid @RequestBody BloodInventoryRequest request) {
        return ResponseEntity.ok(bloodBankService.updateInventory(id, request));
    }

    @GetMapping("/donors")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PageResponse<BloodDonorResponse>> getDonors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(bloodBankService.getDonors(page, size));
    }

    @PostMapping("/donors")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<BloodDonorResponse> createDonor(@Valid @RequestBody BloodDonorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bloodBankService.createDonor(request));
    }

    @GetMapping("/requests")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PageResponse<BloodRequestResponse>> getRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(bloodBankService.getRequests(page, size));
    }

    @PostMapping("/requests")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<BloodRequestResponse> createRequest(@Valid @RequestBody BloodRequestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bloodBankService.createRequest(request));
    }

    @PatchMapping("/requests/{id}/fulfill")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<BloodRequestResponse> fulfillRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(bloodBankService.fulfillRequest(id));
    }
}
