package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RoomRequest {

    @NotBlank
    private String roomNumber;

    @NotNull
    private RoomType roomType;

    private Integer floor;
    private UUID departmentId;
    private BigDecimal dailyRate;
    private Boolean active;
}
