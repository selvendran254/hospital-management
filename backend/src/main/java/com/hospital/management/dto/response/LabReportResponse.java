package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabReportResponse {

    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID labTestId;
    private String labTestName;
    private UUID appointmentId;
    private String resultSummary;
    private String reportFileUrl;
    private Boolean completed;
    private Instant completedAt;
    private Instant createdAt;
}
