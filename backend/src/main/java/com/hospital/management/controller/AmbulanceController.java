package com.hospital.management.controller;

import com.hospital.management.dto.request.AmbulanceRequest;
import com.hospital.management.dto.response.AmbulanceResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.service.AmbulanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ambulances")
@RequiredArgsConstructor
@Tag(name = "Ambulances")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PageResponse<AmbulanceResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ambulanceService.getAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AmbulanceResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ambulanceService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AmbulanceResponse> create(@Valid @RequestBody AmbulanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ambulanceService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AmbulanceResponse> update(@PathVariable UUID id,
                                                    @Valid @RequestBody AmbulanceRequest request) {
        return ResponseEntity.ok(ambulanceService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        ambulanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
