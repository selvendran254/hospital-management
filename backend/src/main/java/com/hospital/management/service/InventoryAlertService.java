package com.hospital.management.service;

import com.hospital.management.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryAlertService {

    private final MedicineRepository medicineRepository;
    private final EmailService emailService;

    public void triggerLowStockAlerts() {
        medicineRepository.findLowStockMedicines().forEach(medicine ->
                emailService.sendTemplateEmail(
                        "admin@hospital.com",
                        "inventory-alert",
                        "Low stock alert: " + medicine.getName(),
                        java.util.Map.of("medicine", medicine.getName(), "qty", String.valueOf(medicine.getStockQuantity()))
                ));
    }
}
