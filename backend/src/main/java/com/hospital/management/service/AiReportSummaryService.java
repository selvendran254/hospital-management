package com.hospital.management.service;

import com.hospital.management.dto.request.FeatureRequests;
import com.hospital.management.dto.response.FeatureResponses;
import org.springframework.stereotype.Service;

@Service
public class AiReportSummaryService {

    public FeatureResponses.AiSummaryResponse summarizeLabReport(FeatureRequests.LabResultRequest request) {
        int abnormalCount = request.getAbnormalValues().size();
        String risk = abnormalCount >= 3 ? "HIGH" : abnormalCount >= 1 ? "MEDIUM" : "LOW";
        String summary = "Patient " + request.getPatientName() + " has " + abnormalCount
                + " abnormal finding(s). Observations reviewed: " + String.join(", ", request.getObservations()) + ".";
        if (!request.getAbnormalValues().isEmpty()) {
            summary = summary + " Abnormal values: " + String.join(", ", request.getAbnormalValues()) + ".";
        }
        return FeatureResponses.AiSummaryResponse.builder()
                .summary(summary)
                .riskLevel(risk)
                .build();
    }
}
