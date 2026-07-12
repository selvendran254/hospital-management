package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.BloodGroup;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BloodInventoryRequest {

    @NotNull
    private BloodGroup bloodGroup;

    @NotNull
    private Integer unitsAvailable;
}
