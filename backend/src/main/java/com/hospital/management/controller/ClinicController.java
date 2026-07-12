package com.hospital.management.controller;

import com.hospital.management.domain.entity.Clinic;
import com.hospital.management.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @PostMapping
    public ResponseEntity<Clinic> create(@Valid @RequestBody Clinic clinic) {
        return ResponseEntity.ok(clinicService.create(clinic));
    }

    @GetMapping
    public ResponseEntity<List<Clinic>> list() {
        return ResponseEntity.ok(clinicService.list());
    }
}
