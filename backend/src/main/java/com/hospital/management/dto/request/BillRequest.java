package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class BillRequest {

    @NotNull
    private UUID patientId;

    private UUID appointmentId;
    private UUID admissionId;

    @NotNull
    private BigDecimal totalAmount;

    private String description;
    private String gstNumber;
    private BigDecimal cgst;
    private BigDecimal sgst;
}
