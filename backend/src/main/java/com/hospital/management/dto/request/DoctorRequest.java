package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.Gender;
import com.hospital.management.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DoctorRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private UUID departmentId;
    private String specialization;
    private String qualification;
    private Integer experienceYears;
    private Gender gender;
    private String phone;
    private String bio;
    private BigDecimal consultationFee;
    private Boolean available;
}
