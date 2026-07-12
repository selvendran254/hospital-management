package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.BloodGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BloodInventoryResponse {

    private UUID id;
    private BloodGroup bloodGroup;
    private Integer unitsAvailable;
    private Instant updatedAt;
}
