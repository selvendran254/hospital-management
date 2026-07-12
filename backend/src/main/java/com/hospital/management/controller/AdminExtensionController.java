package com.hospital.management.controller;

import com.hospital.management.domain.entity.HospitalBranch;
import com.hospital.management.domain.entity.PayrollRecord;
import com.hospital.management.domain.entity.Permission;
import com.hospital.management.dto.request.ExtensionRequests;
import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin/ext")
@RequiredArgsConstructor
public class AdminExtensionController {

    private final ExtensionCrudService extensionCrudService;
    private final InventoryAlertService inventoryAlertService;
    private final ExportService exportService;
    private final RevenueForecastService revenueForecastService;

    @PostMapping("/payroll")
    public ResponseEntity<PayrollRecord> createPayroll(@Valid @RequestBody ExtensionRequests.PayrollRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createPayroll(request));
    }

    @PostMapping("/branches")
    public ResponseEntity<HospitalBranch> createBranch(@Valid @RequestBody ExtensionRequests.HospitalBranchRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createBranch(request));
    }

    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody ExtensionRequests.PermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(extensionCrudService.createPermission(request));
    }

    @PostMapping("/inventory/alerts")
    public ResponseEntity<Void> triggerAlerts() {
        inventoryAlertService.triggerLowStockAlerts();
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/export/csv")
    public ResponseEntity<ExtensionResponses.ExportFileResponse> exportCsv() {
        return ResponseEntity.ok(exportService.exportCsv(
                List.of("module", "status"),
                List.of(List.of("admin", "ok"), List.of("inventory", "ok"))));
    }

    @GetMapping("/export/excel")
    public ResponseEntity<ExtensionResponses.ExportFileResponse> exportExcel() {
        return ResponseEntity.ok(exportService.exportExcel(
                List.of("module", "status"),
                List.of(List.of("admin", "ok"), List.of("inventory", "ok"))));
    }

    @GetMapping("/revenue-forecast")
    public ResponseEntity<ExtensionResponses.RevenueForecastResponse> forecast(
            @RequestParam(defaultValue = "100000") BigDecimal lastMonthRevenue,
            @RequestParam(defaultValue = "0.05") BigDecimal growthRate,
            @RequestParam(defaultValue = "6") int months) {
        return ResponseEntity.ok(revenueForecastService.forecast(lastMonthRevenue, growthRate, months));
    }
}
