package com.hospital.management.service;

import com.hospital.management.domain.entity.Supplier;
import com.hospital.management.dto.request.SupplierRequest;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.dto.response.SupplierResponse;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.SupplierRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final EntityMapper entityMapper;

    public PageResponse<SupplierResponse> getAll(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "name", "asc");
        return PageResponse.from(supplierRepository.findAll(pageable).map(entityMapper::toSupplierResponse));
    }

    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .contactPerson(request.getContactPerson())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
        return entityMapper.toSupplierResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public SupplierResponse update(UUID id, SupplierRequest request) {
        Supplier supplier = findSupplier(id);
        supplier.setName(request.getName());
        supplier.setContactPerson(request.getContactPerson());
        supplier.setPhone(request.getPhone());
        supplier.setEmail(request.getEmail());
        supplier.setAddress(request.getAddress());
        return entityMapper.toSupplierResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public void delete(UUID id) {
        supplierRepository.delete(findSupplier(id));
    }

    private Supplier findSupplier(UUID id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found: " + id));
    }
}
