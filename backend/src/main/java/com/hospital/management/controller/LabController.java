package com.hospital.management.controller;

import com.hospital.management.dto.request.LabReportRequest;
import com.hospital.management.dto.request.LabTestRequest;
import com.hospital.management.dto.response.LabReportResponse;
import com.hospital.management.dto.response.LabTestResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.service.LabService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/lab")
@RequiredArgsConstructor
@Tag(name = "Laboratory")
public class LabController {

    private final LabService labService;

    @GetMapping("/tests")
    public ResponseEntity<PageResponse<LabTestResponse>> getTests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(labService.getTests(page, size));
    }

    @GetMapping("/tests/{id}")
    public ResponseEntity<LabTestResponse> getTestById(@PathVariable UUID id) {
        return ResponseEntity.ok(labService.getTestById(id));
    }

    @PostMapping("/tests")
    @PreAuthorize("hasAnyRole('ADMIN', 'LABORATORY_STAFF')")
    public ResponseEntity<LabTestResponse> createTest(@Valid @RequestBody LabTestRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(labService.createTest(request));
    }

    @PutMapping("/tests/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LABORATORY_STAFF')")
    public ResponseEntity<LabTestResponse> updateTest(@PathVariable UUID id,
                                                      @Valid @RequestBody LabTestRequest request) {
        return ResponseEntity.ok(labService.updateTest(id, request));
    }

    @DeleteMapping("/tests/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTest(@PathVariable UUID id) {
        labService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN', 'LABORATORY_STAFF', 'DOCTOR')")
    public ResponseEntity<PageResponse<LabReportResponse>> getReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(labService.getReports(page, size, status));
    }

    @GetMapping("/reports/my")
    public ResponseEntity<PageResponse<LabReportResponse>> getMyReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(labService.getMyReports(page, size));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<LabReportResponse> getReportById(@PathVariable UUID id) {
        return ResponseEntity.ok(labService.getReportById(id));
    }

    @PutMapping("/reports/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'LABORATORY_STAFF')")
    public ResponseEntity<LabReportResponse> updateReport(@PathVariable UUID id,
                                                          @Valid @RequestBody LabReportRequest request) {
        return ResponseEntity.ok(labService.updateReport(id, request));
    }
}
