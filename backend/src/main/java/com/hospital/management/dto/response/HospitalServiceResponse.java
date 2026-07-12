package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalServiceResponse {

    private UUID id;
    private String name;
    private String description;
    private String icon;
    private UUID departmentId;
    private String departmentName;
    private Boolean active;
}
