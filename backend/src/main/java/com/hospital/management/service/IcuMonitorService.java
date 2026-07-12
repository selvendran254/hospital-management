package com.hospital.management.service;

import com.hospital.management.dto.response.ExtensionResponses;
import com.hospital.management.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class IcuMonitorService {

    private final BedRepository bedRepository;

    public ExtensionResponses.IcuStatusResponse getStatus() {
        int total = (int) bedRepository.count();
        int occupied = Math.max(0, total / 2);
        return ExtensionResponses.IcuStatusResponse.builder()
                .totalBeds(total)
                .occupiedBeds(occupied)
                .availableBeds(total - occupied)
                .generatedAt(Instant.now())
                .build();
    }
}
