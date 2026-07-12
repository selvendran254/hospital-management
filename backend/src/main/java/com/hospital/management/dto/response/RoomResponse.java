package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {

    private UUID id;
    private String roomNumber;
    private RoomType roomType;
    private Integer floor;
    private UUID departmentId;
    private BigDecimal dailyRate;
    private Boolean active;
}
