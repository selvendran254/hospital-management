package com.hospital.management.service;

import com.hospital.management.domain.entity.BloodDonor;
import com.hospital.management.domain.entity.BloodInventory;
import com.hospital.management.domain.entity.BloodRequest;
import com.hospital.management.dto.request.BloodDonorRequest;
import com.hospital.management.dto.request.BloodInventoryRequest;
import com.hospital.management.dto.request.BloodRequestRequest;
import com.hospital.management.dto.response.*;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.BloodDonorRepository;
import com.hospital.management.repository.BloodInventoryRepository;
import com.hospital.management.repository.BloodRequestRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BloodBankService {

    private final BloodInventoryRepository inventoryRepository;
    private final BloodDonorRepository donorRepository;
    private final BloodRequestRepository requestRepository;
    private final EntityMapper entityMapper;

    public List<BloodInventoryResponse> getInventory() {
        return inventoryRepository.findAll().stream()
                .map(entityMapper::toBloodInventoryResponse)
                .toList();
    }

    @Transactional
    public BloodInventoryResponse updateInventory(UUID id, BloodInventoryRequest request) {
        BloodInventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blood inventory not found"));
        inventory.setUnitsAvailable(request.getUnitsAvailable());
        return entityMapper.toBloodInventoryResponse(inventoryRepository.save(inventory));
    }

    public PageResponse<BloodDonorResponse> getDonors(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "name", "asc");
        return PageResponse.from(donorRepository.findAll(pageable).map(entityMapper::toBloodDonorResponse));
    }

    @Transactional
    public BloodDonorResponse createDonor(BloodDonorRequest request) {
        BloodDonor donor = BloodDonor.builder()
                .name(request.getName())
                .bloodGroup(request.getBloodGroup())
                .phone(request.getPhone())
                .email(request.getEmail())
                .lastDonationDate(request.getLastDonationDate())
                .address(request.getAddress())
                .build();
        return entityMapper.toBloodDonorResponse(donorRepository.save(donor));
    }

    public PageResponse<BloodRequestResponse> getRequests(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "requestedAt", "desc");
        return PageResponse.from(requestRepository.findAll(pageable).map(entityMapper::toBloodRequestResponse));
    }

    @Transactional
    public BloodRequestResponse createRequest(BloodRequestRequest request) {
        BloodRequest bloodRequest = BloodRequest.builder()
                .patientId(request.getPatientId())
                .bloodGroup(request.getBloodGroup())
                .unitsRequired(request.getUnitsRequired())
                .urgency(request.getUrgency())
                .status("PENDING")
                .build();
        return entityMapper.toBloodRequestResponse(requestRepository.save(bloodRequest));
    }

    @Transactional
    public BloodRequestResponse fulfillRequest(UUID id) {
        BloodRequest bloodRequest = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blood request not found"));
        if ("FULFILLED".equalsIgnoreCase(bloodRequest.getStatus())) {
            throw new BadRequestException("Request already fulfilled");
        }

        BloodInventory inventory = inventoryRepository.findByBloodGroup(bloodRequest.getBloodGroup())
                .orElseThrow(() -> new BadRequestException("No inventory for blood group"));
        if (inventory.getUnitsAvailable() < bloodRequest.getUnitsRequired()) {
            throw new BadRequestException("Insufficient blood units available");
        }

        inventory.setUnitsAvailable(inventory.getUnitsAvailable() - bloodRequest.getUnitsRequired());
        inventoryRepository.save(inventory);

        bloodRequest.setStatus("FULFILLED");
        bloodRequest.setFulfilledAt(Instant.now());
        return entityMapper.toBloodRequestResponse(requestRepository.save(bloodRequest));
    }
}
