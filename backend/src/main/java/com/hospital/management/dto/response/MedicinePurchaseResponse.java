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
public class MedicinePurchaseResponse {

    private UUID id;
    private UUID medicineId;
    private String medicineName;
    private UUID supplierId;
    private String supplierName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private LocalDate purchaseDate;
    private String invoiceNumber;
}
