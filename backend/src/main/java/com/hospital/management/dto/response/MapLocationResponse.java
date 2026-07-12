package com.hospital.management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapLocationResponse {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String googleMapsUrl;
    private String directionsUrl;
}
