package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.BloodGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BloodRequestRequest {

    private UUID patientId;

    @NotNull
    private BloodGroup bloodGroup;

    private Integer unitsRequired;
    private String urgency;
}
