package com.hospital.management.controller;

import com.hospital.management.dto.request.DoctorAvailabilityRequest;
import com.hospital.management.dto.request.DoctorLeaveRequest;
import com.hospital.management.dto.request.DoctorRequest;
import com.hospital.management.dto.response.*;
import com.hospital.management.domain.enums.Gender;
import com.hospital.management.service.DoctorService;
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
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping
    public ResponseEntity<PageResponse<DoctorResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience) {
        return ResponseEntity.ok(doctorService.getAll(page, size, query, sortBy, sortDir,
                departmentId, gender, available, minExperience, maxExperience));
    }

    @GetMapping("/public")
    public ResponseEntity<List<DoctorResponse>> getPublic() {
        return ResponseEntity.ok(doctorService.getPublicDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(doctorService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponse> create(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<DoctorResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.ok(doctorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        doctorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDashboardResponse> getDashboard() {
        return ResponseEntity.ok(doctorService.getDashboard());
    }

    @GetMapping("/schedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<DoctorAvailabilityResponse>> getSchedule(
            @RequestParam(required = false) UUID doctorId) {
        return ResponseEntity.ok(doctorService.getSchedule(doctorId));
    }

    @PutMapping("/schedule")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<DoctorAvailabilityResponse>> updateSchedule(
            @Valid @RequestBody List<DoctorAvailabilityRequest> requests) {
        return ResponseEntity.ok(doctorService.updateSchedule(requests));
    }

    @GetMapping("/leaves")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PageResponse<DoctorLeaveResponse>> getLeaves(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(doctorService.getLeaves(page, size));
    }

    @PostMapping("/leaves")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorLeaveResponse> requestLeave(@Valid @RequestBody DoctorLeaveRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.requestLeave(request));
    }

    @GetMapping("/patients")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PageResponse<PatientResponse>> getPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(doctorService.getPatients(page, size));
    }
}
