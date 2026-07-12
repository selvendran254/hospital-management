package com.hospital.management.service;

import com.hospital.management.domain.entity.Medicine;
import com.hospital.management.dto.request.MedicineRequest;
import com.hospital.management.dto.response.MedicineResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.MedicineRepository;
import com.hospital.management.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final EntityMapper entityMapper;

    public PageResponse<MedicineResponse> getAll(int page, int size, String query) {
        Pageable pageable = PageUtils.of(page, size, "name", "asc");
        if (query != null && !query.isBlank()) {
            return PageResponse.from(medicineRepository.findByNameContainingIgnoreCase(query, pageable)
                    .map(entityMapper::toMedicineResponse));
        }
        return PageResponse.from(medicineRepository.findAll(pageable).map(entityMapper::toMedicineResponse));
    }

    public MedicineResponse getById(UUID id) {
        return entityMapper.toMedicineResponse(findMedicine(id));
    }

    @Transactional
    public MedicineResponse create(MedicineRequest request) {
        Medicine medicine = Medicine.builder()
                .name(request.getName())
                .genericName(request.getGenericName())
                .category(request.getCategory())
                .manufacturer(request.getManufacturer())
                .unitPrice(request.getUnitPrice())
                .stockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0)
                .reorderLevel(request.getReorderLevel() != null ? request.getReorderLevel() : 10)
                .expiryDate(request.getExpiryDate())
                .batchNumber(request.getBatchNumber())
                .barcode(request.getBarcode())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();
        return entityMapper.toMedicineResponse(medicineRepository.save(medicine));
    }

    @Transactional
    public MedicineResponse update(UUID id, MedicineRequest request) {
        Medicine medicine = findMedicine(id);
        medicine.setName(request.getName());
        medicine.setGenericName(request.getGenericName());
        medicine.setCategory(request.getCategory());
        medicine.setManufacturer(request.getManufacturer());
        medicine.setUnitPrice(request.getUnitPrice());
        if (request.getStockQuantity() != null) medicine.setStockQuantity(request.getStockQuantity());
        if (request.getReorderLevel() != null) medicine.setReorderLevel(request.getReorderLevel());
        medicine.setExpiryDate(request.getExpiryDate());
        medicine.setBatchNumber(request.getBatchNumber());
        medicine.setBarcode(request.getBarcode());
        if (request.getActive() != null) medicine.setActive(request.getActive());
        return entityMapper.toMedicineResponse(medicineRepository.save(medicine));
    }

    @Transactional
    public void delete(UUID id) {
        Medicine medicine = findMedicine(id);
        medicine.setActive(false);
        medicineRepository.save(medicine);
    }

    public List<MedicineResponse> getLowStock() {
        return medicineRepository.findLowStockMedicines().stream()
                .map(entityMapper::toMedicineResponse)
                .toList();
    }

    private Medicine findMedicine(UUID id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found: " + id));
    }
}
