package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.ParkingSlotType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParkingSlotRequest {
    @NotBlank
    private String slotNumber;
    private String floorLevel;
    private ParkingSlotType slotType;
}
