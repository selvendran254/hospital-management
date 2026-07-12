package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.BloodGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BloodDonorRequest {

    @NotBlank
    private String name;

    @NotNull
    private BloodGroup bloodGroup;

    private String phone;
    private String email;
    private LocalDate lastDonationDate;
    private String address;
}
