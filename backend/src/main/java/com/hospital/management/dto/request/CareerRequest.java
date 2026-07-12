package com.hospital.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CareerRequest {

    @NotBlank
    private String title;

    private UUID departmentId;

    @NotBlank
    private String description;

    private String requirements;
    private String location;
    private Boolean active;
}
