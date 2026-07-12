package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AmbulanceRequest {

    @NotBlank
    private String vehicleNumber;

    private String driverName;

    @NotBlank
    private String driverPhone;

    private Boolean available;
    private String location;
}
