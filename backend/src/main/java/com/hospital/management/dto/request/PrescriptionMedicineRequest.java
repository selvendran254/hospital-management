package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrescriptionMedicineRequest {

    @NotBlank
    private String medicineName;

    private String dosage;
    private String frequency;
    private String duration;
}
