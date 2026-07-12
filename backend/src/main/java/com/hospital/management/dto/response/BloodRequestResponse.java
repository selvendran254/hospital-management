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
public class BloodRequestResponse {

    private UUID id;
    private UUID patientId;
    private String patientName;
    private BloodGroup bloodGroup;
    private Integer unitsRequired;
    private String urgency;
    private String status;
    private Instant requestedAt;
    private Instant fulfilledAt;
}
