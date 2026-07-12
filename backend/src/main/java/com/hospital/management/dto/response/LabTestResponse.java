package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabTestResponse {

    private UUID id;
    private String name;
    private String code;
    private String barcode;
    private String description;
    private BigDecimal price;
    private UUID departmentId;
    private Boolean active;
}
