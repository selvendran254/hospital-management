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
public class AmbulanceResponse {

    private UUID id;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private Boolean available;
    private String location;
}
