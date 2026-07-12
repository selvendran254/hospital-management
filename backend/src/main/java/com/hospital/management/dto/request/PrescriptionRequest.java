package com.hospital.management.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PrescriptionRequest {

    private UUID appointmentId;

    @NotNull
    private UUID patientId;

    @NotNull
    private UUID doctorId;

    private String diagnosis;
    private String instructions;

    @Valid
    private List<PrescriptionMedicineRequest> medicines;
}
