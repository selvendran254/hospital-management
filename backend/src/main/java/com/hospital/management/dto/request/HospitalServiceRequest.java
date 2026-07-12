package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class HospitalServiceRequest {

    @NotBlank
    private String name;

    private String description;
    private String icon;
    private UUID departmentId;
    private Boolean active;
}
