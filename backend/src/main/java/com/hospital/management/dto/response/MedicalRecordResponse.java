package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {

    private UUID id;
    private UUID patientId;
    private UUID doctorId;
    private String doctorName;
    private LocalDate visitDate;
    private String chiefComplaint;
    private String diagnosis;
    private String treatment;
    private Map<String, Object> vitals;
    private Instant createdAt;
}
