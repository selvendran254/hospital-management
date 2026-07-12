package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class MedicinePurchaseRequest {

    @NotNull
    private UUID medicineId;

    private UUID supplierId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

    private LocalDate purchaseDate;
    private String invoiceNumber;
}
