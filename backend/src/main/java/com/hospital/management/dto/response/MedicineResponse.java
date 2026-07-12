package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineResponse {

    private UUID id;
    private String name;
    private String genericName;
    private String manufacturer;
    private String category;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private String barcode;
    private UUID supplierId;
    private String supplierName;
    private Boolean active;
    private Boolean lowStock;
}
