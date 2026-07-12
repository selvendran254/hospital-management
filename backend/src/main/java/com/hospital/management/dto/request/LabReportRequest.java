package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LabReportRequest {

    @NotNull
    private UUID patientId;

    @NotNull
    private UUID labTestId;

    private UUID appointmentId;
    private String resultSummary;
    private String reportFileUrl;
    private Boolean completed;
}
