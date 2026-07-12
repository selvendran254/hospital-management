package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AdmissionRequest {

    @NotNull
    private UUID patientId;

    @NotNull
    private UUID bedId;

    private String diagnosis;
    private String notes;
}
