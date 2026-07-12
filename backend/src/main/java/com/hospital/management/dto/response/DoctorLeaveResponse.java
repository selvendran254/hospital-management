package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorLeaveResponse {

    private UUID id;
    private UUID doctorId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
