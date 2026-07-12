package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class ParkingReserveRequest {
    @NotBlank
    private String visitorName;
    @NotBlank
    private String visitorPhone;
    @NotBlank
    private String vehicleNumber;
    private UUID slotId;
}
