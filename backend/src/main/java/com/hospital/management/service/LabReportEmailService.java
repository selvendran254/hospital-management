package com.hospital.management.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LabReportEmailService {

    private final EmailService emailService;

    public void sendLabReportReady(String toEmail, String patientName) {
        emailService.sendTemplateEmail(
                toEmail,
                "lab-report-ready",
                "Lab report is available",
                Map.of("patientName", patientName)
        );
    }
}
