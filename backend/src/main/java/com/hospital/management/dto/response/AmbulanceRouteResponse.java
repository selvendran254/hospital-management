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
public class AmbulanceRouteResponse {
    private UUID id;
    private String vehicleNumber;
    private String driverName;
    private String driverPhone;
    private String location;
    private Double latitude;
    private Double longitude;
    private Double destinationLat;
    private Double destinationLng;
    private String routeUrl;
    private Boolean available;
}
