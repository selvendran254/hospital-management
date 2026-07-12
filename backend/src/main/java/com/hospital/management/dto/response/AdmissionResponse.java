package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.AdmissionStatus;
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
public class AdmissionResponse {

    private UUID id;
    private UUID patientId;
    private String patientName;
    private UUID bedId;
    private String bedNumber;
    private String roomNumber;
    private UUID admittedBy;
    private Instant admissionDate;
    private Instant dischargeDate;
    private AdmissionStatus status;
    private String diagnosis;
    private String notes;
}
