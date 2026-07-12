package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class LabTestRequest {

    @NotBlank
    private String name;

    private String code;
    private String description;

    @NotNull
    private BigDecimal price;

    private String barcode;
    private UUID departmentId;
    private Boolean active;
}
