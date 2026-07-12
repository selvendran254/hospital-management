package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MedicineSaleRequest {

    @NotNull
    private UUID medicineId;

    private UUID patientId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;
}
