package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.BloodGroup;
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
public class BloodDonorResponse {

    private UUID id;
    private String name;
    private BloodGroup bloodGroup;
    private String phone;
    private String email;
    private LocalDate lastDonationDate;
    private String address;
}
