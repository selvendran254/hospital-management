package com.hospital.management.controller;

import com.hospital.management.dto.response.CareerResponse;
import com.hospital.management.dto.response.HealthPackageResponse;
import com.hospital.management.dto.response.HospitalServiceResponse;
import com.hospital.management.service.PublicService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Tag(name = "Public")
public class PublicController {

    private final PublicService publicService;

    @GetMapping("/services")
    public ResponseEntity<List<HospitalServiceResponse>> getServices() {
        return ResponseEntity.ok(publicService.getServices());
    }

    @GetMapping("/health-packages")
    public ResponseEntity<List<HealthPackageResponse>> getHealthPackages() {
        return ResponseEntity.ok(publicService.getHealthPackages());
    }

    @GetMapping("/careers")
    public ResponseEntity<List<CareerResponse>> getCareers() {
        return ResponseEntity.ok(publicService.getCareers());
    }
}
