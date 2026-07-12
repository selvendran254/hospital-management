package com.hospital.management.controller;

import com.hospital.management.dto.request.MedicinePurchaseRequest;
import com.hospital.management.dto.request.MedicineSaleRequest;
import com.hospital.management.dto.response.MedicinePurchaseResponse;
import com.hospital.management.dto.response.MedicineSaleResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.service.PharmacyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pharmacy")
@RequiredArgsConstructor
@Tag(name = "Pharmacy")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @GetMapping("/purchases")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<PageResponse<MedicinePurchaseResponse>> getPurchases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(pharmacyService.getPurchases(page, size));
    }

    @PostMapping("/purchases")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<MedicinePurchaseResponse> createPurchase(
            @Valid @RequestBody MedicinePurchaseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.createPurchase(request));
    }

    @GetMapping("/sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<PageResponse<MedicineSaleResponse>> getSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(pharmacyService.getSales(page, size));
    }

    @PostMapping("/sales")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<MedicineSaleResponse> createSale(@Valid @RequestBody MedicineSaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pharmacyService.createSale(request));
    }
}
