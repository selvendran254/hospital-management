package com.hospital.management.dto.request;

import com.hospital.management.domain.enums.BedStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BedRequest {

    @NotNull
    private UUID roomId;

    @NotBlank
    private String bedNumber;

    private BedStatus status;
}
