package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class MedicineRequest {

    @NotBlank
    private String name;

    private String genericName;
    private String manufacturer;
    private String category;

    @NotNull
    private BigDecimal unitPrice;

    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private String batchNumber;
    private String barcode;
    private UUID supplierId;
    private Boolean active;
}
