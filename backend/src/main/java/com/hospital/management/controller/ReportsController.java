package com.hospital.management.controller;

import com.hospital.management.dto.response.FeatureResponses;
import com.hospital.management.service.DoctorPerformanceService;
import com.hospital.management.service.FinancialYearReportService;
import com.hospital.management.service.MonthlyReportService;
import com.hospital.management.service.PatientDemographicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final MonthlyReportService monthlyReportService;
    private final DoctorPerformanceService doctorPerformanceService;
    private final PatientDemographicsService patientDemographicsService;
    private final FinancialYearReportService financialYearReportService;

    @GetMapping("/monthly")
    public ResponseEntity<FeatureResponses.MonthlyStatsResponse> monthly(@RequestParam int year, @RequestParam int month) {
        return ResponseEntity.ok(monthlyReportService.summarize(year, month));
    }

    @GetMapping("/monthly/pdf")
    public ResponseEntity<byte[]> monthlyPdf(@RequestParam int year, @RequestParam int month) {
        byte[] pdf = monthlyReportService.generatePdf(year, month);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment().filename("monthly-report-" + year + "-" + month + ".pdf").build().toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/doctor-performance")
    public ResponseEntity<List<FeatureResponses.DoctorPerformanceRow>> doctorPerformance() {
        return ResponseEntity.ok(doctorPerformanceService.rankDoctors());
    }

    @GetMapping("/patient-demographics")
    public ResponseEntity<FeatureResponses.PatientDemographicsResponse> demographics() {
        return ResponseEntity.ok(patientDemographicsService.analyze());
    }

    @GetMapping("/financial-year")
    public ResponseEntity<FeatureResponses.FinancialReportResponse> financial(@RequestParam int financialYearStart) {
        return ResponseEntity.ok(financialYearReportService.generate(financialYearStart));
    }
}
