package com.hospital.management.controller;

import com.hospital.management.dto.request.BillRequest;
import com.hospital.management.dto.request.PaymentRequest;
import com.hospital.management.dto.response.BillResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.PaymentResponse;
import com.hospital.management.service.BillService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Billing")
public class BillingController {

    private final BillService billService;

    @GetMapping("/bills")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PageResponse<BillResponse>> getBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(billService.getAll(page, size));
    }

    @GetMapping("/bills/my")
    public ResponseEntity<PageResponse<BillResponse>> getMyBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(billService.getMyBills(page, size));
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<BillResponse> getBillById(@PathVariable UUID id) {
        return ResponseEntity.ok(billService.getById(id));
    }

    @PostMapping("/bills")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<BillResponse> createBill(@Valid @RequestBody BillRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.create(request));
    }

    @PostMapping("/payments")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PaymentResponse> recordPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.recordPayment(request));
    }
}
