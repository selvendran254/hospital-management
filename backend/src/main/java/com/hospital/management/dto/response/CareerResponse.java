package com.hospital.management.dto.response;

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
public class CareerResponse {

    private UUID id;
    private String title;
    private UUID departmentId;
    private String departmentName;
    private String description;
    private String requirements;
    private String location;
    private Boolean active;
    private Instant postedAt;
}
