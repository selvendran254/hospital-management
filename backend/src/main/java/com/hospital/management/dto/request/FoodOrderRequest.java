package com.hospital.management.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class FoodOrderRequest {
    @NotNull
    private UUID menuItemId;
    @Min(1)
    private Integer quantity = 1;
    private String roomNumber;
    private String notes;
    private UUID patientId;
}
