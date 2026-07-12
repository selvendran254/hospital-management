package com.hospital.management.service;

import com.hospital.management.domain.entity.Ambulance;
import com.hospital.management.dto.request.AmbulanceRequest;
import com.hospital.management.dto.response.AmbulanceResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.AmbulanceRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;
    private final EntityMapper entityMapper;

    public PageResponse<AmbulanceResponse> getAll(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "vehicleNumber", "asc");
        return PageResponse.from(ambulanceRepository.findAll(pageable).map(entityMapper::toAmbulanceResponse));
    }

    public AmbulanceResponse getById(UUID id) {
        return entityMapper.toAmbulanceResponse(findAmbulance(id));
    }

    @Transactional
    public AmbulanceResponse create(AmbulanceRequest request) {
        Ambulance ambulance = Ambulance.builder()
                .vehicleNumber(request.getVehicleNumber())
                .driverName(request.getDriverName())
                .driverPhone(request.getDriverPhone())
                .available(request.getAvailable() != null ? request.getAvailable() : true)
                .location(request.getLocation())
                .build();
        return entityMapper.toAmbulanceResponse(ambulanceRepository.save(ambulance));
    }

    @Transactional
    public AmbulanceResponse update(UUID id, AmbulanceRequest request) {
        Ambulance ambulance = findAmbulance(id);
        ambulance.setVehicleNumber(request.getVehicleNumber());
        ambulance.setDriverName(request.getDriverName());
        ambulance.setDriverPhone(request.getDriverPhone());
        if (request.getAvailable() != null) ambulance.setAvailable(request.getAvailable());
        ambulance.setLocation(request.getLocation());
        return entityMapper.toAmbulanceResponse(ambulanceRepository.save(ambulance));
    }

    @Transactional
    public void delete(UUID id) {
        ambulanceRepository.delete(findAmbulance(id));
    }

    private Ambulance findAmbulance(UUID id) {
        return ambulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ambulance not found: " + id));
    }
}
