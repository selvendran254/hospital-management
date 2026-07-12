package com.hospital.management.dto.response;

import com.hospital.management.domain.enums.BedStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BedResponse {

    private UUID id;
    private UUID roomId;
    private String roomNumber;
    private String bedNumber;
    private BedStatus status;
}
