package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private UUID id;
    private UUID userId;
    private String email;
    private UUID departmentId;
    private String departmentName;
    private String firstName;
    private String lastName;
    private String fullName;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private Gender gender;
    private String phone;
    private String bio;
    private BigDecimal consultationFee;
    private Boolean available;
    private Instant createdAt;
}
