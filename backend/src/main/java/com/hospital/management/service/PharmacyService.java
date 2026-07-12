package com.hospital.management.service;

import com.hospital.management.domain.entity.Medicine;
import com.hospital.management.domain.entity.MedicinePurchase;
import com.hospital.management.domain.entity.MedicineSale;
import com.hospital.management.domain.entity.User;
import com.hospital.management.dto.request.MedicinePurchaseRequest;
import com.hospital.management.dto.request.MedicineSaleRequest;
import com.hospital.management.dto.response.MedicinePurchaseResponse;
import com.hospital.management.dto.response.MedicineSaleResponse;
import com.hospital.management.dto.response.PageResponse;
import com.hospital.management.exception.BadRequestException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.mapper.EntityMapper;
import com.hospital.management.repository.MedicinePurchaseRepository;
import com.hospital.management.repository.MedicineRepository;
import com.hospital.management.repository.MedicineSaleRepository;
import com.hospital.management.repository.SupplierRepository;
import com.hospital.management.util.PageUtils;
import com.hospital.management.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PharmacyService {

    private final MedicinePurchaseRepository purchaseRepository;
    private final MedicineSaleRepository saleRepository;
    private final MedicineRepository medicineRepository;
    private final SupplierRepository supplierRepository;
    private final EntityMapper entityMapper;
    private final SecurityUtils securityUtils;

    public PageResponse<MedicinePurchaseResponse> getPurchases(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "purchaseDate", "desc");
        return PageResponse.from(purchaseRepository.findAll(pageable).map(entityMapper::toMedicinePurchaseResponse));
    }

    @Transactional
    public MedicinePurchaseResponse createPurchase(MedicinePurchaseRequest request) {
        if (request.getSupplierId() != null) {
            supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        }
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));

        BigDecimal total = request.getUnitPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        MedicinePurchase purchase = MedicinePurchase.builder()
                .supplierId(request.getSupplierId())
                .medicineId(request.getMedicineId())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .purchaseDate(request.getPurchaseDate() != null ? request.getPurchaseDate() : java.time.LocalDate.now())
                .invoiceNumber(request.getInvoiceNumber())
                .build();
        purchase = purchaseRepository.save(purchase);

        medicine.setStockQuantity(medicine.getStockQuantity() + request.getQuantity());
        medicineRepository.save(medicine);
        return entityMapper.toMedicinePurchaseResponse(purchase);
    }

    public PageResponse<MedicineSaleResponse> getSales(int page, int size) {
        Pageable pageable = PageUtils.of(page, size, "saleDate", "desc");
        return PageResponse.from(saleRepository.findAll(pageable).map(entityMapper::toMedicineSaleResponse));
    }

    @Transactional
    public MedicineSaleResponse createSale(MedicineSaleRequest request) {
        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found"));
        if (medicine.getStockQuantity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock");
        }

        User user = securityUtils.getCurrentUser();
        BigDecimal unitPrice = request.getUnitPrice() != null ? request.getUnitPrice() : medicine.getUnitPrice();
        BigDecimal total = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        MedicineSale sale = MedicineSale.builder()
                .medicineId(request.getMedicineId())
                .patientId(request.getPatientId())
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .totalAmount(total)
                .soldBy(user.getId())
                .build();
        sale = saleRepository.save(sale);

        medicine.setStockQuantity(medicine.getStockQuantity() - request.getQuantity());
        medicineRepository.save(medicine);
        return entityMapper.toMedicineSaleResponse(sale);
    }
}
