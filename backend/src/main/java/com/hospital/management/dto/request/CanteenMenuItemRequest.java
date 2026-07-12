package com.hospital.management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CanteenMenuItemRequest {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private BigDecimal price;
    private String category;
    private Boolean veg;
    private Boolean available;
    private String imageUrl;
    private UUID clinicId;
}
